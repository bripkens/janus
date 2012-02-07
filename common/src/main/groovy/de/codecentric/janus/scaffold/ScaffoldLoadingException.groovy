package de.codecentric.janus.scaffold

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldLoadingException extends RuntimeException {
    ScaffoldLoadingException(Throwable cause) {
        super(cause)
    }

    ScaffoldLoadingException() {
    }

    ScaffoldLoadingException(String message) {
        super(message)
    }

    ScaffoldLoadingException(String message, Throwable cause) {
        super(message, cause)
    }
}
