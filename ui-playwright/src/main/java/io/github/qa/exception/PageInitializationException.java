package io.github.qa.exception;

/**
 * Exception thrown when a Playwright Page cannot be created or configured.
 */
public class PageInitializationException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Failed to create Playwright Page.";

    public PageInitializationException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
