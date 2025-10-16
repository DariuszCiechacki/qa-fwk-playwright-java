package io.github.qa.config.exception;

/**
 * Thrown when the playwright configuration cannot be loaded or parsed.
 */
public class ConfigurationLoadException extends RuntimeException {
    public ConfigurationLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}