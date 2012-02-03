package de.codecentric.janus.ci.jenkins

import de.codecentric.janus.ci.jenkins.conf.ServiceConfiguration
import de.codecentric.janus.conf.Project
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.ANY
import static groovyx.net.http.ContentType.XML
import org.apache.http.HttpRequestInterceptor
import org.apache.http.protocol.HttpContext
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ProjectCreator {
    private final ServiceConfiguration serviceConfiguration
    private final ProjectConfigurationGenerator configurationGenerator

    ProjectCreator(ServiceConfiguration serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration
        configurationGenerator = new ProjectConfigurationGenerator()
    }

    boolean create(Project project) {
        def config = configurationGenerator.generate(project)

        def http = new HTTPBuilder(serviceConfiguration.uri)

        // HTTP builder needs to be put in preemptive mode since Jenkins
        // directly responds with 403 instead of 401.
        // Therefore http.auth.basic(name, apiToken) isn't used
        def encodedCredentials = (serviceConfiguration.username + ':' +
                serviceConfiguration.apiToken).bytes.encodeBase64().toString()
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
            body = config

            println config
            println uri.toString()
            
            response.success = { HttpResponse resp ->
                successful = true
                println resp.getClass().getName()
                println resp.statusLine
            }

            response.failure = { HttpResponse resp ->
                println resp.getClass().getName()
                println "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
            }
        }

        successful
    }
}
