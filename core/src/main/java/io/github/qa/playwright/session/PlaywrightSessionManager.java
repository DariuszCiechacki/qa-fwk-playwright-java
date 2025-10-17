package io.github.qa.playwright.session;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.github.qa.playwright.context.BrowserContextFactory;
import io.github.qa.playwright.page.PageFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PlaywrightSessionManager {
    private static final ThreadLocal<PlaywrightSessionManager> THREAD_SESSION =
            ThreadLocal.withInitial(PlaywrightSessionManager::new);

    private BrowserContext context;
    private Page page;

    private PlaywrightSessionManager() {
        // Prevent external construction
    }

    /**
     * Initializes a fresh BrowserContext and Page for the current test.
     * A single Browser is reused per thread, as managed by {@link io.github.qa.playwright.browser.BrowserFactory}.
     */
    public static void startPlaywrightSession() {
        PlaywrightSessionManager session = THREAD_SESSION.get();
        session.initializePlaywrightSessionStack();
        log.info("[{}] Playwright session started.", Thread.currentThread().getName());
    }

    /**
     * Returns the current thread's {@link PlaywrightSessionManager} instance.
     */
    public static PlaywrightSessionManager getCurrentSession() {
        return THREAD_SESSION.get();
    }

    /**
     * Final teardown — closes the Browser (per thread) after all tests in this thread/class are done.
     */
    public static void sessionTeardown() {
        THREAD_SESSION.remove();
    }

    private void initializePlaywrightSessionStack() {
        this.context = BrowserContextFactory.createContext();
        this.page = PageFactory.createPage(this.context);
    }

    /**
     * Returns the current {@link BrowserContext}.
     *
     * @throws IllegalStateException if the context has not been initialized.
     */
    public BrowserContext getCurrentSessionContext() {
        ensureInitialized(this.context, "BrowserContext not initialized. Call startPlaywrightSession() first.");
        return this.context;
    }

    /**
     * Returns the current {@link Page}.
     *
     * @throws IllegalStateException if the page has not been initialized.
     */
    public Page getCurrentSessionPage() {
        ensureInitialized(this.page, "Page not initialized. Call startPlaywrightSession() first.");
        return this.page;
    }

    /**
     * Closes the current BrowserContext (and all its Pages).
     * Should be called after each test.
     */
    public void closeSession() {
        if (this.context != null) {
            BrowserContextFactory.closeContext(this.context);
            this.context = null;
            this.page = null;
            log.info("[{}] Playwright session context closed.", Thread.currentThread().getName());
        }
    }

    private void ensureInitialized(Object obj, String message) {
        if (obj == null) {
            throw new IllegalStateException(message);
        }
    }
}
