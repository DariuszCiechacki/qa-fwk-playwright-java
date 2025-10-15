package io.github.qa.playwright.session;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.github.qa.playwright.PlaywrightManager;
import io.github.qa.playwright.browser.BrowserFactory;
import io.github.qa.playwright.context.BrowserContextFactory;

/**
 * Central orchestrator managing the lifecycle of Playwright components.
 * <p>
 * Provides unified access to {@link com.microsoft.playwright.Page}, {@link com.microsoft.playwright.BrowserContext}, and {@link com.microsoft.playwright.Browser},
 * and ensures correct creation and cleanup order.
 * </p>
 */
public final class PlaywrightSessionManager {
    private static final ThreadLocal<PlaywrightSessionManager> THREAD_SESSION =
        ThreadLocal.withInitial(PlaywrightSessionManager::new);

    private Browser browser;
    private BrowserContext context;
    private Page page;

    private PlaywrightSessionManager() {
        // Prevent external construction
    }

    /**
     * Returns the thread-local Playwright session manager instance.
     */
    public static PlaywrightSessionManager current() {
        return THREAD_SESSION.get();
    }

    /**
     * Initializes a full Playwright session stack (Browser → Context → Page).
     */
    public void startSession() {
        this.browser = BrowserFactory.getBrowser();
        this.context = BrowserContextFactory.createContext();
        this.page = this.context.newPage();
    }

    /**
     * Returns the current Browser.
     */
    public Browser getBrowser() {
        ensureInitialized(browser, "Browser not initialized. Call startSession() first.");
        return browser;
    }

    /**
     * Returns the current BrowserContext.
     */
    public BrowserContext getContext() {
        ensureInitialized(context, "Context not initialized. Call startSession() first.");
        return context;
    }

    /**
     * Returns the current Playwright Page.
     */
    public Page getPage() {
        ensureInitialized(page, "Page not initialized. Call startSession() first.");
        return page;
    }

    /**
     * Closes all resources in the correct order.
     */
    public void closeSession() {
        try {
            if (page != null) {
                page.close();
            }
            if (context != null) {
                context.close();
            }
        } finally {
            // Always close browser and Playwright at the very end
            if (browser != null) {
                browser.close();
            }
            PlaywrightManager.close();
            THREAD_SESSION.remove();
        }
    }

    private void ensureInitialized(Object obj, String message) {
        if (obj == null) {
            throw new IllegalStateException(message);
        }
    }
}
