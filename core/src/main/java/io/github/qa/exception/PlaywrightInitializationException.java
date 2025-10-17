package io.github.qa.exception;

/**
 * Exception thrown when Playwright fails to initialize or cannot start properly.
 */
public class PlaywrightInitializationException extends RuntimeException {
    public PlaywrightInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
