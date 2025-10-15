package io.github.qa.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents configuration for Playwright BrowserContext behavior.
 */
@Getter
@Setter
public class ContextConfig {
    /** Whether Playwright should ignore HTTPS certificate errors. */
    private boolean ignoreHTTPSErrors;

    /** Browser locale (e.g., "en-US", "pl-PL"). */
    private String locale;

    /** Default viewport dimensions used when creating a browser context. */
    private ViewportConfig viewport;
}
