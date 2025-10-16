package io.github.qa.playwright.browser;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

/**
 * Resolves the WebKit browser type (Safari engine).
 */
public class WebkitResolver implements BrowserTypeResolver {
    @Override
    public BrowserType resolve(Playwright playwright) {
        return playwright.webkit();
    }
}
