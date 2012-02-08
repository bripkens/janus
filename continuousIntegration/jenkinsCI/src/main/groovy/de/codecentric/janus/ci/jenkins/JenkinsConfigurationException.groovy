package de.codecentric.janus.ci.jenkins

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JenkinsConfigurationException extends RuntimeException {
    JenkinsConfigurationException() {
    }

    JenkinsConfigurationException(String message) {
        super(message)
    }

    JenkinsConfigurationException(String message, Throwable cause) {
        super(message, cause)
    }

    JenkinsConfigurationException(Throwable cause) {
        super(cause)
    }
}
