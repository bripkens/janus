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
    static final JOB_FILE_REGEX = /^jobs\/jenkins\/job(-([a-z0-9-_]+))?\.xml$/
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
        def sortedEntries = getSortedEntries(zip)
        sortedEntries.each { k, v ->
            applyJob(zip, k, v, context)
        }

        try {
            zip.close()
        } catch (IOException ex) {
        }
    }

    private NavigableMap<String, ZipEntry> getSortedEntries(ZipFile zip) {
        def sortedEntries = [] as TreeMap<String, ZipEntry>

        def entries = zip.entries()
        while(entries.hasMoreElements()) {
            def entry = entries.nextElement()

            if (!entry.directory && entry.name.startsWith('jobs/jenkins/')) {
                def jobName = generateJobName(entry.name)
                sortedEntries[jobName] = entry
            }
        }

        sortedEntries
    }

    /**
     * Generate an appropriate job name as it can be send to Jenkins
     *
     * Example 1:
     * In: jobs/jenkins/job-main.xml
     * Out: ${project.name}-main
     *
     * Example 2:
     * In: jobs/jenkins/job-main.xml
     * Out: ${project.name}
     *
     * @param filePath The path of the ZIP file entry
     * @return The job name with prefixed project name
     */
    private String generateJobName(String filePath) {
        def matcher = filePath =~ JOB_FILE_REGEX

        if (!matcher.matches()) {
            throw new JenkinsConfigurationException('The Jenkins job ' +
                    "definition file ${filePath} does not use a isValid" +
                    ' format.')
        }

        def customName = matcher[0][2]
        if (customName == null) {
            project.name
        } else {
            project.name + '-' + customName
        }
    }

    private void applyJob(ZipFile zip, String jobName, ZipEntry entry,
                           Map context) {
        def reader = zip.getInputStream(entry).newReader()
        def template = new SimpleTemplateEngine().createTemplate(reader)
        createJob(jobName, template.make(context).toString())
        IOUtils.closeQuietly(reader)
    }

    private void createJob(String jobName, String jobConfig) {
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

        log.info "Creating new Jenkis job ${jobName}."
        log.debug "Configuration for Jenkins job '${jobName}' is ${jobConfig}"
        http.request(POST, ANY) {
            uri.path = '/createItem'
            uri.query = [name: jobName]
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
