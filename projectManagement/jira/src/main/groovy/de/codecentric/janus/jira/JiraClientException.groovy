package de.codecentric.janus.jira

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraClientException extends JiraException {
    JiraClientException() {
    }

    JiraClientException(String message) {
        super(message)
    }

    JiraClientException(String message, Throwable cause) {
        super(message, cause)
    }

    JiraClientException(Throwable cause) {
        super(cause)
    }
}
