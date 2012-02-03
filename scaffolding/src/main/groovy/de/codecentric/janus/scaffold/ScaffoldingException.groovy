package de.codecentric.janus.scaffold

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldingException extends RuntimeException {
    ScaffoldingException(Throwable cause) {
        super(cause)
    }

    ScaffoldingException(String message, Throwable cause) {
        super(message, cause)
    }

    ScaffoldingException(String message) {
        super(message)
    }

    ScaffoldingException() {
    }
}
