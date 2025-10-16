package io.github.qa.exception;

/**
 * Exception thrown when a Playwright Page cannot be created or configured.
 */
public class PageInitializationException extends RuntimeException {
    public PageInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
