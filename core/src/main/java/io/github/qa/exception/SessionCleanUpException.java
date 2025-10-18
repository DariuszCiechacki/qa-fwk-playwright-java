package io.github.qa.exception;

/**
 * Exception thrown when a session fails to clean up.
 */
public class SessionCleanUpException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Failed to clean up session.";

    public SessionCleanUpException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
