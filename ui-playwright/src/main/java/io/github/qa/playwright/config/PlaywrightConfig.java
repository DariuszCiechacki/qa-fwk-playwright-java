package io.github.qa.playwright.config;

import io.github.qa.playwright.config.browser.BrowserConfig;
import io.github.qa.playwright.config.context.ContextConfig;
import io.github.qa.playwright.config.debugging.DebuggingConfig;
import io.github.qa.playwright.config.page.PageConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * Root configuration class representing all configurable aspects of the Playwright.
 * Loaded from {@code playwright-config.yml} via {@link PlaywrightConfigProvider}.
 */
@Getter
@Setter
public class PlaywrightConfig {

    private BrowserConfig browserConfig;
    private ContextConfig contextConfig;
    private PageConfig pageConfig;
    private DebuggingConfig debuggingConfig;
}
