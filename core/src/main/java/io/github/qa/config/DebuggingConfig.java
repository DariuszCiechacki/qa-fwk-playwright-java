package io.github.qa.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents debugging and failure-handling configuration for the framework.
 */
@Getter
@Setter
public class DebuggingConfig {
    /** Whether to automatically capture a screenshot when a test fails. */
    private boolean screenshotsOnFailure;
}
