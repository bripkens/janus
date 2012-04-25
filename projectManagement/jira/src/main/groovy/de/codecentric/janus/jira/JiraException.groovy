package de.codecentric.janus.jira

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraException extends RuntimeException {
    JiraException() {
    }

    JiraException(String message) {
        super(message)
    }

    JiraException(String message, Throwable cause) {
        super(message, cause)
    }

    JiraException(Throwable cause) {
        super(cause)
    }
}
