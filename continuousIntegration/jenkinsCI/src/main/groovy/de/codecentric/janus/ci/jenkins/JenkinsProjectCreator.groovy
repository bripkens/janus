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
import de.codecentric.janus.scaffold.BuildJob

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Slf4j
class JenkinsProjectCreator {
    static final JOB_FILE_REGEX = /^jobs\/jenkins\/job(-([a-z0-9-_]+))?\.xml$/
    final ServiceConfig serviceConfig
    final Project project
    final VCSConfig vcs
    final Scaffold scaffold
    final Map context

    JenkinsProjectCreator(ServiceConfig serviceConfig, Project project,
                          VCSConfig vcs, Scaffold scaffold, Map context) {
        this.serviceConfig = serviceConfig
        this.project = project
        this.vcs = vcs
        this.scaffold = scaffold
        this.context = context
    }

    void create() {
        scaffold.buildJobs.each {BuildJob job ->
            def name = "${project.name}-${job.name}"
            def config = new JenkinsConfigGenerator(project, job, context, vcs)
                    .generate()
            createJob(name, config)
        }
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
