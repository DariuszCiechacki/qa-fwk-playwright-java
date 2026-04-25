package io.github.qa.playwright.browser.channel;

import com.microsoft.playwright.BrowserType;

/**
 * Defines how to apply a specific Chromium channel
 * (e.g. Chrome, Edge) to the {@link BrowserType.LaunchOptions}.
 */
// ToDo: verify if its needed - Playwright resolves channel itself.
public interface ChromiumChannelResolver {

    /**
     * Applies a specific Chromium channel to the given launch options.
     */
    void applyChannel(BrowserType.LaunchOptions options);
}
