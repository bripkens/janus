package de.codecentric.janus.jira

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class Session {
    final String baseUrl
    final Credentials credentials

    JiraSoapSession jiraSoapSession
    JiraSoapClient jiraSoapClient

    Session(String baseUrl) {
        this.baseUrl = baseUrl
    }

    Session(String baseUrl, String name, String password) {
        this.baseUrl = baseUrl
        credentials = new Credentials(name, password)
    }

    void close() {
        jiraSoapSession.close()
    }

    private JiraSoapSession getJiraSoapSession() {
        if (jiraSoapSession == null) {
            jiraSoapSession = new JiraSoapSession(baseUrl, credentials)
        }
        return jiraSoapSession
    }

    JiraSoapClient getJiraSoapClient() {
        if (jiraSoapClient == null) {
            jiraSoapClient = new JiraSoapClientImpl(getJiraSoapSession())
        }
        return jiraSoapClient

    }
}
