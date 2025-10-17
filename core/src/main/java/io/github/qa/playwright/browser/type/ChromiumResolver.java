package io.github.qa.playwright.browser.type;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

/**
 * Resolves the Chromium-based browser (e.g., Chrome, Edge).
 */
public class ChromiumResolver implements BrowserTypeResolver {
    
    @Override
    public BrowserType resolve(Playwright playwright) {
        return playwright.chromium();
    }
}
