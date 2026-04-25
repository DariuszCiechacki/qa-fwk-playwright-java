package io.github.qa.core.exception;

/**
 * Thrown when a module configuration file cannot be loaded or parsed.
 */
public class ConfigurationLoadException extends RuntimeException {

    public ConfigurationLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
