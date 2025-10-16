package io.github.qa.exception;

/**
 * Exception thrown when a browser fails to initialize or launch.
 */
public class BrowserInitializationException extends RuntimeException {
    public BrowserInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
