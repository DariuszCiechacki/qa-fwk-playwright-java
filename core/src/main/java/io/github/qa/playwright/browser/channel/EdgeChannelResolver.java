package io.github.qa.playwright.browser.channel;

import com.microsoft.playwright.BrowserType;

/**
 * Resolver for the Microsoft Edge browser channel.
 */
public class EdgeChannelResolver implements ChromiumChannelResolver {
    @Override
    public BrowserType.LaunchOptions applyChannel(BrowserType.LaunchOptions options) {
        return options.setChannel("msedge");
    }
}
