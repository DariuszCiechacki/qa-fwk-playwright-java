package io.github.qa.playwright.page;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.github.qa.exception.PageInitializationException;
import io.github.qa.playwright.config.PlaywrightConfigProvider;
import io.github.qa.playwright.config.page.PageConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory responsible for creating and configuring new {@link Page} instances.
 * Each page is created within its own {@link BrowserContext}, ensuring full isolation
 * between tests running in parallel. Page-level timeouts and navigation settings are
 * applied from {@link PageConfig} defined in <code>playwright-config.yml</code>.
 */
@Slf4j
public final class PageFactory {

    private PageFactory() {
    }

    /**
     * Creates a new {@link com.microsoft.playwright.Page} with all default configuration applied.
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

            if (pageConfig.getDefaultTimeout() > 0) {
                page.setDefaultTimeout(pageConfig.getDefaultTimeout());
            }
            if (pageConfig.getNavigationTimeout() > 0) {
                page.setDefaultNavigationTimeout(pageConfig.getNavigationTimeout());
            }

            log.info("[{}] New Playwright Page created)", Thread.currentThread().getName());
            return page;
        } catch (Exception e) {
            throw new PageInitializationException(e);
        }
    }

    /**
     * Closes the provided {@link Page}, if active.
     *
     * @param page the page to close
     */
    public static void closePage(Page page) {
        if (page != null) {
            try {
                page.close();
                log.info("[{}] Page closed successfully.", Thread.currentThread().getName());
            } catch (Exception e) {
                log.warn("[{}] Failed to close Page cleanly: {}", Thread.currentThread().getName(), e.getMessage());
            }
        }
    }
}
