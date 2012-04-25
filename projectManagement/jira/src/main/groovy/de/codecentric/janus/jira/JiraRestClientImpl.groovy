package de.codecentric.janus.jira

import groovyx.net.http.HTTPBuilder
import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.HttpResponse
import org.apache.http.protocol.HttpContext

import static groovyx.net.http.ContentType.ANY
import static groovyx.net.http.Method.GET
import groovy.json.JsonSlurper
import org.apache.http.HttpEntity
import de.codecentric.janus.jira.model.RemoteGroupSummary
import de.codecentric.janus.jira.model.RemoteProjectSummary

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraRestClientImpl implements JiraRestClient {
    private final Credentials credentials
    private final String baseUrl

    JiraRestClientImpl(Credentials credentials, String baseUrl) {
        this.credentials = credentials
        this.baseUrl = baseUrl
    }

    @Override
    RemoteGroupSummary[] getGroups() {
        def result = []

        get(baseUrl, PATH.GET_ALL_GROUPS) { HttpResponse resp ->
            HttpEntity entity = resp.entity

            def slurper = new JsonSlurper()
            def json = slurper.parse(entity.content.newReader('UTF-8'))

            json.groups.each {
                result.add(new RemoteGroupSummary(it.name))
            }

            entity.consumeContent()
        }

        return result
    }

    @Override
    RemoteProjectSummary[] getProjects() {
        def result = null

        get(baseUrl, PATH.GET_ALL_PROJECTS) { HttpResponse resp ->
            HttpEntity entity = resp.entity

            def slurper = new JsonSlurper()
            def json = slurper.parse(entity.content.newReader('UTF-8'))

            json.each {
                result.add(new RemoteProjectSummary(it.id, it.key, it.name))
            }

            entity.consumeContent()
        }

        return result
    }

    private get(String baseUrl, String path, Closure success) {
        HTTPBuilder http = getHTTPBuilder()

        http.request(GET, ANY) {
            uri.path = path
            response.success = success
            response.failure = defaultFailureHandler
        }
    }

    private HTTPBuilder getHTTPBuilder() {
        HTTPBuilder http = new HTTPBuilder(baseUrl)

        // HTTP builder needs to be put in preemptive mode since requests to
        // the JIRA REST web service may fail silently, i.e., this just
        // produce no result. Therefore http.auth.basic(name, apiToken) isn't
        // used
        String encodedCredentials = (credentials.name +
                ':' + credentials.password).bytes.encodeBase64().toString()
        http.client.addRequestInterceptor(new HttpRequestInterceptor() {
            void process(HttpRequest httpRequest, HttpContext httpContext) {
                httpRequest.addHeader('Authorization',
                        "Basic ${encodedCredentials}")
            }
        })

        return http
    }

    private Closure getDefaultFailureHandler() {
        return { HttpResponse resp ->
            resp.entity.consumeContent()
            throw new JiraClientException('Unexpected error ' +
                    resp.statusLine.statusCode + ': ' +
                    resp.statusLine.reasonPhrase)
        }
    }

    private static interface PATH {
        String BASE_PATH = '/rest/api/2/'
        String GET_ALL_GROUPS = BASE_PATH + 'groups/picker'
        String GET_ALL_PROJECTS = BASE_PATH + 'project'
    }
}
