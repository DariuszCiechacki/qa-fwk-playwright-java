package io.github.qa.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents configuration for Playwright Page behavior.
 */
@Getter
@Setter
public class PageConfig {
    /** Default timeout (in milliseconds) for actions performed on page elements. */
    private int defaultTimeout;

    /** Timeout (in milliseconds) for page navigation operations. */
    private int navigationTimeout;
}
