package io.github.qa.playwright.browser.channel;

import lombok.Getter;

@Getter
public enum ChromiumChannelName {
    CHROME("chrome"),
    MSEDGE("msedge");

    private final String value;

    ChromiumChannelName(String value) {
        this.value = value;
    }

    /**
     * Returns the canonical enum value (e.g. "chrome") if a match is found.
     * Throws an exception otherwise.
     */
    public static String getByName(String name) {
        for (ChromiumChannelName channel : values()) {
            if (channel.value.equalsIgnoreCase(name.trim())) {
                return channel.value;
            }
        }
        throw new IllegalArgumentException("Unsupported chromium channel: " + name);
    }
}
