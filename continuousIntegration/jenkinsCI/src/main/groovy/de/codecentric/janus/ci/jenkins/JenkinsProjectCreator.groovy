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

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Slf4j
class JenkinsProjectCreator {
    final ServiceConfig serviceConfig
    final Project project
    final Scaffold scaffold

    JenkinsProjectCreator(ServiceConfig serviceConfiguration,
                          Project project, Scaffold scaffold) {
        this.serviceConfig = serviceConfiguration
        this.project = project
        this.scaffold = scaffold
    }

    boolean apply() {
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

        def successful = false

        http.request(POST, ANY) {
            uri.path = '/createItem'
            uri.query = [name: project.name]
            requestContentType = XML
            body = requestBody

            response.success = { HttpResponse resp ->
                successful = true
            }

            response.failure = { HttpResponse resp ->
                throw new JenkinsConfigurationException(
                        "Unexpected error: ${resp.statusLine.statusCode}: " +
                                resp.statusLine.reasonPhrase)
            }
        }

        successful
    }
    
    private String getRequestBody() {
    }
}
