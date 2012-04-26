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
        confluenceSoapSession.close()
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
