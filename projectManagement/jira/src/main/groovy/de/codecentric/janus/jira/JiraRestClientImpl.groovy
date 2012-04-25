/*
 * Copyright (C) 2012 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.atlassian.jira.rpc.soap.beans.RemoteUser

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
        def result = []

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

    @Override
    RemoteUser[] searchUser(String name) {
        def result = []

        get(baseUrl, PATH.GET_SEARCH_USER, [username: name]) { HttpResponse resp ->
            HttpEntity entity = resp.entity

            def slurper = new JsonSlurper()
            def json = slurper.parse(entity.content.newReader('UTF-8'))

            json.each {
                result.add(new RemoteUser(it.emailAddress,
                        it.displayName,
                        it.name))
            }

            entity.consumeContent()
        }

        return result
    }

    private get(String baseUrl, String path, Closure success) {
        get(baseUrl, path, [] as HashMap, success)
    }

    private get(String baseUrl, String path, Map query, Closure success) {
        HTTPBuilder http = getHTTPBuilder()

        http.request(GET, ANY) {
            uri.path = path
            uri.query = query
            response.success = success
            response.failure = defaultFailureHandler
        }
    }

    private HTTPBuilder getHTTPBuilder() {
        HTTPBuilder http = new HTTPBuilder(baseUrl)

        // HTTP builder needs to be put in preemptive mode since requests to
        // the JIRA REST web service may fail silently, i.e., they just
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
        String GET_SEARCH_USER = BASE_PATH + 'user/search'
    }
}
