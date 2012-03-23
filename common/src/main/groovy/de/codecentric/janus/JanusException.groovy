package de.codecentric.janus;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class JanusException extends RuntimeException {
    public JanusException() {
    }

    public JanusException(String message) {
        super(message);
    }

    public JanusException(String message, Throwable cause) {
        super(message, cause);
    }

    public JanusException(Throwable cause) {
        super(cause);
    }
}
