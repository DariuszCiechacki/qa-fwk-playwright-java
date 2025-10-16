package io.github.qa.playwright.browser.channel;

import com.microsoft.playwright.BrowserType;

/**
 * Defines how to apply a specific Chromium channel
 * (e.g. Chrome, Edge) to the {@link BrowserType.LaunchOptions}.
 */
public interface ChromiumChannelResolver {

    /**
     * Applies the channel to the provided LaunchOptions.
     *
     * @param options browser launch options
     * @return modified launch options
     */
    BrowserType.LaunchOptions applyChannel(BrowserType.LaunchOptions options);
}
