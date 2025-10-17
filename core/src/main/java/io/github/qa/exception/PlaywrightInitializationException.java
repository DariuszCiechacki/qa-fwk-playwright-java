package io.github.qa.exception;

/**
 * Exception thrown when Playwright fails to initialize or cannot start properly.
 */
public class PlaywrightInitializationException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Failed to initialize Playwright.";

    public PlaywrightInitializationException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
