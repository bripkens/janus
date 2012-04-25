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

import com.atlassian.jira.rpc.soap.JiraSoapServiceServiceLocator
import com.atlassian.jira.rpc.soap.JiraSoapService
import com.atlassian.jira.rpc.soap.JiraSoapServiceService
import javax.xml.rpc.ServiceException
import groovy.util.logging.Slf4j

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Slf4j
class JiraSoapSession {
    static final String ENDPOINT_EXTENSION = '/rpc/soap/jirasoapservice-v2'

    final JiraSoapServiceService serviceLocator
    final JiraSoapService service
    final String authToken
    final Credentials credentials
    final URL endpoint

    JiraSoapSession(String baseUrl, Credentials credentials) {
        assert baseUrl != null

        endpoint = new URL(baseUrl + ENDPOINT_EXTENSION)
        serviceLocator = new JiraSoapServiceServiceLocator();
        this.credentials = credentials

        try {
            service = serviceLocator
                    .getJirasoapserviceV2(endpoint);
            log.info("Connected to ${endpoint.toExternalForm()}")
        } catch (ServiceException e) {
            throw new JiraConnectionException("ServiceException during " +
                    "SOAPClient contruction", e);
        }

        if (credentials != null) {
            authToken = service.login(credentials.name, credentials.password)
            log.info("Logged into Jira SOAP service.")
        }
    }

    def close() {
        if (authToken != null) {
            service.logout(authToken)
        }
    }
}
