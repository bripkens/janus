package de.codecentric.janus.jira

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraConnectionException extends JiraException {
    JiraConnectionException() {
    }

    JiraConnectionException(String message) {
        super(message)
    }

    JiraConnectionException(String message, Throwable cause) {
        super(message, cause)
    }

    JiraConnectionException(Throwable cause) {
        super(cause)
    }
}
