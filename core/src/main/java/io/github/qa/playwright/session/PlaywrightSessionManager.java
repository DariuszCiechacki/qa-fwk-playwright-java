package io.github.qa.playwright.session;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.github.qa.exception.SessionCleanUpException;
import io.github.qa.exception.SessionCloseException;
import io.github.qa.playwright.PlaywrightManager;
import io.github.qa.playwright.browser.BrowserFactory;
import io.github.qa.playwright.context.BrowserContextFactory;
import io.github.qa.playwright.page.PageFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Central orchestrator managing the lifecycle of Playwright components
 * for a single test thread.
 * <p>
 * Each test thread maintains its own Browser instance (via {@link BrowserFactory}),
 * and each test within that thread creates an isolated {@link BrowserContext} and {@link Page}.
 * </p>
 */
@Slf4j
public final class PlaywrightSessionManager {

    private static final ThreadLocal<PlaywrightSessionManager> THREAD_SESSION =
            ThreadLocal.withInitial(PlaywrightSessionManager::new);

    @Getter
    private BrowserContext context;
    @Getter
    private Page page;

    private PlaywrightSessionManager() {
    }

    /**
     * Initializes Playwright stack for the current test:
     * <ul>
     *   <li>Initializes Playwright (per thread)</li>
     *   <li>Creates Browser (per thread)</li>
     *   <li>Creates new Context and Page (per test)</li>
     * </ul>
     */
    public static void startPlaywrightSession() {
        PlaywrightSessionManager session = THREAD_SESSION.get();
        session.initializeStack();
        log.info("[{}] Playwright session started.", Thread.currentThread().getName());
    }

    /**
     * Returns the current thread's PlaywrightSessionManager instance.
     */
    public static PlaywrightSessionManager getCurrentSession() {
        return THREAD_SESSION.get();
    }

    /**
     * Performs final cleanup after all tests:
     * closes Browser and Playwright for this thread.
     */
    public static void cleanUp() {
        try {
            BrowserFactory.close();
            PlaywrightManager.close();
            THREAD_SESSION.remove();
            log.info("[{}] Playwright session cleaned up.", Thread.currentThread().getName());
        } catch (Exception e) {
            throw new SessionCleanUpException(e);
        }
    }

    private void initializeStack() {
        PlaywrightManager.initialize();
        BrowserFactory.initialize();
        this.context = BrowserContextFactory.createContext();
        this.page = PageFactory.createPage(this.context);
    }

    /**
     * Closes BrowserContext (and its Pages) after each test.
     */
    public void closeSession() {
        if (context != null) {
            try {
                context.close();
                log.info("[{}] BrowserContext closed.", Thread.currentThread().getName());
            } catch (Exception e) {
                throw new SessionCloseException(e);
            } finally {
                context = null;
                page = null;
            }
        }
    }
}
