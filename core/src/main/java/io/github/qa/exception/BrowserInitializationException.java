package io.github.qa.exception;

/**
 * Exception thrown when a browser fails to initialize or launch.
 */
public class BrowserInitializationException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Failed to create browser instance.";

    public BrowserInitializationException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
