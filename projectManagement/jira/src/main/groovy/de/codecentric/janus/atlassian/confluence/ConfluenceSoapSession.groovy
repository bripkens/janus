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

package de.codecentric.janus.atlassian.confluence

import com.atlassian.confluence.rpc.soap.ConfluenceSoapService
import com.atlassian.confluence.rpc.soap.ConfluenceSoapServiceService
import com.atlassian.confluence.rpc.soap.ConfluenceSoapServiceServiceLocator
import groovy.util.logging.Slf4j

import javax.xml.rpc.ServiceException

import de.codecentric.janus.atlassian.Credentials
import de.codecentric.janus.atlassian.AtlassianConnectionException

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Slf4j
class ConfluenceSoapSession {
    static final String ENDPOINT_EXTENSION = '/rpc/soap-axis/confluenceservice-v2'

    final ConfluenceSoapServiceService serviceLocator
    final ConfluenceSoapService service
    final String authToken
    final Credentials credentials
    final URL endpoint

    ConfluenceSoapSession(String baseUrl, Credentials credentials) {
        assert baseUrl != null

        endpoint = new URL(baseUrl + ENDPOINT_EXTENSION)
        serviceLocator = new ConfluenceSoapServiceServiceLocator();
        this.credentials = credentials

        try {
            service = serviceLocator.getConfluenceserviceV2(endpoint)
            log.info("Connected to ${endpoint.toExternalForm()}")
        } catch (ServiceException e) {
            throw new AtlassianConnectionException("ServiceException during " +
                    "SOAPClient contruction", e);
        }

        if (credentials != null) {
            authToken = service.login(credentials.name, credentials.password)
            log.info("Logged into Confluence SOAP service.")
        }
    }

    def close() {
        if (authToken != null) {
            service.logout(authToken)
        }
    }
}
