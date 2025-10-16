package io.github.qa.playwright.browser.channel;

import com.microsoft.playwright.BrowserType;

/**
 * Resolver for the Chrome browser channel.
 */
public class ChromeChannelResolver implements ChromiumChannelResolver {
    @Override
    public BrowserType.LaunchOptions applyChannel(BrowserType.LaunchOptions options) {
        return options.setChannel("chrome");
    }
}
