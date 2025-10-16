package io.github.qa.playwright.browser.type;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

/**
 * Resolves the Mozilla Firefox browser type.
 */
public class FirefoxResolver implements BrowserTypeResolver {
    @Override
    public BrowserType resolve(Playwright playwright) {
        return playwright.firefox();
    }
}
