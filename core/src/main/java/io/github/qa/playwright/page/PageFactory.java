package io.github.qa.playwright.page;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.github.qa.config.PageConfig;
import io.github.qa.config.PlaywrightConfigLoader;
import io.github.qa.exception.PageInitializationException;
import io.github.qa.playwright.context.BrowserContextFactory;

/**
 * Factory responsible for creating and configuring new {@link com.microsoft.playwright.Page} instances.
 * <p>
 * Each page is created within its own {@link com.microsoft.playwright.BrowserContext}, applying
 * timeout settings from {@link io.github.qa.config.PageConfig}.
 * </p>
 */
public final class PageFactory {
    private PageFactory() {
        // utility class
    }

    /**
     * Creates a new {@link com.microsoft.playwright.Page} with all default configuration applied.
     *
     * @return fully initialized Playwright Page
     * @throws PageInitializationException if page creation fails
     */
    public static Page createPage() {
        try {
            // Create a new browser context
            BrowserContext context = BrowserContextFactory.createContext();

            // Load page configuration
            PageConfig pageConfig = PlaywrightConfigLoader.get().getConfig().getPageConfig();

            // Create a new page within the context
            Page page = context.newPage();

            if (pageConfig != null) {
                if (pageConfig.getDefaultTimeout() > 0) {
                    page.setDefaultTimeout(pageConfig.getDefaultTimeout());
                }
                if (pageConfig.getNavigationTimeout() > 0) {
                    page.setDefaultNavigationTimeout(pageConfig.getNavigationTimeout());
                }
            }

            return page;

        } catch (Exception e) {
            throw new PageInitializationException("Failed to create Playwright Page", e);
        }
    }
}
