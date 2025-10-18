package io.github.qa.playwright.page;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.github.qa.exception.PageInitializationException;
import io.github.qa.playwright.config.PlaywrightConfigProvider;
import io.github.qa.playwright.config.page.PageConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory responsible for creating new {@link Page} instances within a given {@link BrowserContext}.
 */
@Slf4j
public final class PageFactory {

    private PageFactory() {
    }

    /**
     * Creates a new {@link Page} with all configuration applied.
     *
     * @return fully initialized Playwright Page.
     * @throws PageInitializationException if page creation fails.
     */
    public static Page createPage(BrowserContext context) {
        try {
            if (context == null) {
                throw new IllegalArgumentException("BrowserContext must not be null when creating a Page.");
            }

            // Load page configuration
            PageConfig pageConfig = PlaywrightConfigProvider.get().getConfig().getPageConfig();

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

            log.info("[{}] Playwright Page created.", Thread.currentThread().getName());
            return page;

        } catch (Exception e) {
            throw new PageInitializationException(e);
        }
    }
}
