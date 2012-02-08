package de.codecentric.janus.ci.jenkins

import de.codecentric.janus.conf.Project
import groovyx.net.http.HTTPBuilder
import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.HttpResponse
import org.apache.http.protocol.HttpContext
import static groovyx.net.http.ContentType.ANY
import static groovyx.net.http.ContentType.XML
import static groovyx.net.http.Method.POST
import de.codecentric.janus.scaffold.Scaffold
import groovy.util.logging.Slf4j
import de.codecentric.janus.ci.jenkins.conf.ServiceConfig
import de.codecentric.janus.conf.vcs.VCSConfig
import java.util.zip.ZipFile
import java.util.zip.ZipEntry
import groovy.text.SimpleTemplateEngine
import org.apache.commons.io.IOUtils

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Slf4j
class JenkinsProjectCreator {
    final ServiceConfig serviceConfig
    final Project project
    final Scaffold scaffold
    final VCSConfig vcs

    JenkinsProjectCreator(ServiceConfig serviceConfiguration,
                          Project project,
                          Scaffold scaffold,
                          VCSConfig vcs) {
        this.serviceConfig = serviceConfiguration
        this.project = project
        this.scaffold = scaffold
        this.vcs = vcs
    }

    void applyScaffold() {
        def vcsPartial = new VCSPartialGenerator(vcs, project)
                .generatePartial()

        def zip = new ZipFile(scaffold.file, ZipFile.OPEN_READ)

        def context = ['vcsConfig': vcsPartial, 'project': project]
        getSortedEntries(zip).every { k, v ->
            applyJob(zip, v, context)
        }

        try {
            zip.close()
        } catch (IOException ex) {
        }
        
        // TODO run over every template and run it through the template
        //       engine and sent the result to the server
    }

    private NavigableMap<String, ZipEntry> getSortedEntries(ZipFile zip) {
        def sortedEntries = new TreeMap<String, ZipEntry>()

        def entries = zip.entries()
        while(entries.hasMoreElements()) {
            def entry = entries.nextElement()

            if (!entry.directory && entry.name.startsWith('jobs/jenkins/')) {
                sortedEntries[entry.name] = entry
            }
        }

        sortedEntries
    }

    private void applyJob(ZipFile zip, ZipEntry entry, Map context) {
        def reader = zip.getInputStream(entry).newReader()

        def engine = new SimpleTemplateEngine()
        def template = engine.createTemplate(reader)

        println template.make(context)

        IOUtils.closeQuietly(reader)
    }

    private void createJob(String jobConfig) {
        HTTPBuilder http = new HTTPBuilder(serviceConfig.uri)

        // HTTP builder needs to be put in preemptive mode since Jenkins
        // directly responds with 403 instead of 401.
        // Therefore http.auth.basic(name, apiToken) isn't used
        String encodedCredentials = (serviceConfig.username +
                ':' + serviceConfig.apiToken).bytes.encodeBase64().toString()
        http.client.addRequestInterceptor(new HttpRequestInterceptor() {
            void process(HttpRequest httpRequest, HttpContext httpContext) {
                httpRequest.addHeader('Authorization', "Basic ${encodedCredentials}")
            }
        })

        http.request(POST, ANY) {
            uri.path = '/createItem'
            uri.query = [name: project.name]
            requestContentType = XML
            body = jobConfig

            /*response.success = { HttpResponse resp ->

            }*/

            response.failure = { HttpResponse resp ->
                throw new JenkinsConfigurationException(
                        "Unexpected error: ${resp.statusLine.statusCode}: " +
                                resp.statusLine.reasonPhrase)
            }
        }
    }
}
