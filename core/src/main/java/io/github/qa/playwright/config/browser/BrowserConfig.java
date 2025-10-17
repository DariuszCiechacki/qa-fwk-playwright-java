package io.github.qa.playwright.config.browser;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents configuration for Playwright Browser behavior.
 */
@Getter
@Setter
public class BrowserConfig {

    /**
     * Browser engine type (chromium, firefox, webkit).
     */
    private String type;

    /**
     * Browser channel (chrome, msedge, etc.).
     */
    private String channel;

    /**
     * Whether to run in headless mode.
     */
    private boolean headless;

    /**
     * Artificial delay between actions (milliseconds).
     */
    private int slowMo;
}
