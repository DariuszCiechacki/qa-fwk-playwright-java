package io.github.qa.exception;

/**
 * Exception thrown when a BrowserContext cannot be created or configured.
 */
public class ContextInitializationException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Failed to create browser context.";

    public ContextInitializationException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
