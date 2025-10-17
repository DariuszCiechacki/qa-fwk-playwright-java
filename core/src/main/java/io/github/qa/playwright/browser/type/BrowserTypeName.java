package io.github.qa.playwright.browser.type;

import lombok.Getter;

@Getter
public enum BrowserTypeName {
    CHROMIUM("chromium"),
    FIREFOX("firefox"),
    WEBKIT("webkit");

    private final String value;

    BrowserTypeName(String value) {
        this.value = value;
    }

    /**
     * Returns the canonical enum value (e.g. "chromium") if a match is found.
     * Throws an exception otherwise.
     */
    public static String getByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Browser type cannot be null or empty");
        }
        for (BrowserTypeName type : values()) {
            if (type.value.equalsIgnoreCase(name.trim())) {
                return type.value;
            }
        }
        throw new IllegalArgumentException("Unsupported browser type: " + name);
    }
}
