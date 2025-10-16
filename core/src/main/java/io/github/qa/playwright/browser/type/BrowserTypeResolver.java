package io.github.qa.playwright.browser.type;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

/**
 * Resolves the correct {@link BrowserType} from Playwright based on browser family.
 */
public interface BrowserTypeResolver {
    /**
     * Returns the appropriate {@link BrowserType} for this resolver.
     *
     * @param playwright shared Playwright instance
     * @return resolved BrowserType
     */
    BrowserType resolve(Playwright playwright);
}
