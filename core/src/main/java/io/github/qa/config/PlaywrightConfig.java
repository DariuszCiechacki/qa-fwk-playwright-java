package io.github.qa.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Root configuration class representing all configurable aspects of the Playwright framework.
 * <p>Loaded from {@code playwright-config.yml} via {@link PlaywrightConfigLoader}.</p>
 */
@Getter
@Setter
public class PlaywrightConfig {
    private BrowserConfig browserConfig;
    private ContextConfig contextConfig;
    private PageConfig pageConfig;
    private DebuggingConfig debuggingConfig;
}
