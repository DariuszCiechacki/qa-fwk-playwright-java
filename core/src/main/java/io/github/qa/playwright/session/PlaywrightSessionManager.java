package io.github.qa.playwright.session;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.github.qa.playwright.PlaywrightManager;
import io.github.qa.playwright.browser.BrowserFactory;
import io.github.qa.playwright.context.BrowserContextFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Central orchestrator managing the lifecycle of Playwright components.
 * <p>
 * Provides unified access to {@link com.microsoft.playwright.Page}, {@link com.microsoft.playwright.BrowserContext}, and {@link com.microsoft.playwright.Browser},
 * and ensures correct creation and cleanup order.
 * </p>
 */
@Slf4j
public final class PlaywrightSessionManager {
    private static final ThreadLocal<PlaywrightSessionManager> THREAD_SESSION =
        ThreadLocal.withInitial(PlaywrightSessionManager::new);

    private Browser browser;
    private BrowserContext context;
    private Page page;

    private PlaywrightSessionManager() {
        // Prevent external construction
    }

    public static PlaywrightSessionManager startPlaywrightSession() {
        PlaywrightSessionManager session = THREAD_SESSION.get();
        session.initializePlaywrightSessionStack();
        log.info("[INFO] Playwright session started.");
        return session;
    }

    /**
     * Returns the current thread's PlaywrightSessionManager instance.
     */
    public static PlaywrightSessionManager current() {
        return THREAD_SESSION.get();
    }

    /**
     * Final teardown after class/suite: close Browser + Playwright.
     */
    public static void sessionTeardown() {
        PlaywrightSessionManager session = THREAD_SESSION.get();
        try {
            if (session.browser != null) {
                session.browser.close();
            }
        } finally {
            PlaywrightManager.close();
            THREAD_SESSION.remove();
        }
    }

    /**
     * Initializes a full Playwright session stack (Browser → Context → Page).
     */
    private void initializePlaywrightSessionStack() {
        this.browser = BrowserFactory.getBrowser();
        this.context = BrowserContextFactory.createContext();
        this.page = this.context.newPage();
    }

    /**
     * Returns the current Browser.
     */
    public Browser getBrowser() {
        ensureInitialized(browser, "Browser not initialized. Call startPlaywrightSession() first.");
        return browser;
    }

    /**
     * Returns the current BrowserContext.
     */
    public BrowserContext getContext() {
        ensureInitialized(context, "Context not initialized. Call startPlaywrightSession() first.");
        return context;
    }

    /**
     * Returns the current Playwright Page.
     */
    public Page getPage() {
        ensureInitialized(page, "Page not initialized. Call startPlaywrightSession() first.");
        return page;
    }

    /**
     * Closes only the test-level resources (Page + Context).
     * Browser and Playwright remain active until explicitly shut down.
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
            page = null;
            context = null;
        }
        log.info("[INFO] Playwright session closed.");
    }

    private void ensureInitialized(Object obj, String message) {
        if (obj == null) {
            throw new IllegalStateException(message);
        }
    }
}
