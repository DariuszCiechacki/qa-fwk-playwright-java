package io.github.qa.exception;

/**
 * Exception thrown when a session fails to close.
 */
public class SessionCloseException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Failed to close session.";

    public SessionCloseException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
