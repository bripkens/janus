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

import de.codecentric.janus.atlassian.Credentials
import de.codecentric.janus.atlassian.jira.JiraSoapSession
import de.codecentric.janus.atlassian.jira.JiraSoapClient
import de.codecentric.janus.atlassian.jira.JiraRestClient
import de.codecentric.janus.atlassian.jira.JiraSoapClientImpl

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ConfluenceSession {
    final String baseUrl
    final Credentials credentials

    ConfluenceSoapSession confluenceSoapSession
    ConfluenceSoapClient confluenceSoapClient

    ConfluenceSession(String baseUrl) {
        this.baseUrl = baseUrl
    }

    ConfluenceSession(String baseUrl, String name, String password) {
        this.baseUrl = baseUrl
        credentials = new Credentials(name, password)
    }

    void close() {
        if (confluenceSoapSession != null) {
            confluenceSoapSession.close()
        }
    }

    private ConfluenceSoapSession getConfluenceSoapSession() {
        if (confluenceSoapSession == null) {
            confluenceSoapSession = new ConfluenceSoapSession(baseUrl,
                    credentials)
        }
        return confluenceSoapSession
    }

    ConfluenceSoapClient getConfluenceSoapClient() {
        if (confluenceSoapClient == null) {
            confluenceSoapClient = new ConfluenceSoapClientImpl(
                    getConfluenceSoapSession())
        }
        return confluenceSoapClient
    }
}
