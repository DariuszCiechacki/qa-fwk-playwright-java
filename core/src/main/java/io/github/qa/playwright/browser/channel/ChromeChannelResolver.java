package io.github.qa.playwright.browser.channel;

import com.microsoft.playwright.BrowserType;

/**
 * Resolver for the Chrome browser channel.
 */
public class ChromeChannelResolver implements ChromiumChannelResolver {

    @Override
    public void applyChannel(BrowserType.LaunchOptions options) {
        options.setChannel(ChromiumChannelName.CHROME.getValue());
    }
}
