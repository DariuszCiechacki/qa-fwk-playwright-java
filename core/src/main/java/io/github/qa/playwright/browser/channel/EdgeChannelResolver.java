package io.github.qa.playwright.browser.channel;

import com.microsoft.playwright.BrowserType;

/**
 * Resolver for the Microsoft Edge browser channel.
 */
public class EdgeChannelResolver implements ChromiumChannelResolver {

    @Override
    public void applyChannel(BrowserType.LaunchOptions options) {
        options.setChannel(ChromiumChannelName.MSEDGE.getValue());
    }
}
