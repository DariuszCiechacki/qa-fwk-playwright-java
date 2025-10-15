package io.github.qa.exception;

/**
 * Exception thrown when a BrowserContext cannot be created or configured.
 */
public class ContextInitializationException extends RuntimeException {
    public ContextInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
