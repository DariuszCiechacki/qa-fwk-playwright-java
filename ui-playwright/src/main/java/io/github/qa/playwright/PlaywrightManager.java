package io.github.qa.playwright;

import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;

/**
 * Controls lifecycle of {@link Playwright}.
 * Each test thread maintains its own Playwright instance and closed safely after all tests.
 */
@Slf4j
public final class PlaywrightManager {

    private static final ThreadLocal<Playwright> THREAD_PLAYWRIGHT = new ThreadLocal<>();

    private PlaywrightManager() {
    }

    /**
     * Explicitly initializes {@link Playwright} for the current thread, if not already initialized.
     */
    public static synchronized void initialize() {
        if (THREAD_PLAYWRIGHT.get() == null) {
            Playwright playwright = Playwright.create();
            THREAD_PLAYWRIGHT.set(playwright);
            log.info("[{}] Playwright instance initialized.", Thread.currentThread().getName());
        } else {
            log.debug("[{}] Playwright already initialized; skipping.", Thread.currentThread().getName());
        }
    }

    /**
     * Returns the current {@link Playwright} instance for this thread.
     *
     * @throws IllegalStateException if Playwright has not been initialized.
     */
    public static Playwright getCurrentInstance() {
        Playwright playwright = THREAD_PLAYWRIGHT.get();
        if (playwright == null) {
            throw new IllegalStateException(
                    "Playwright instance not initialized. Call PlaywrightManager.initialize() first.");
        }
        return playwright;
    }

    /**
     * Closes and removes the Playwright instance for the current thread, if active.
     */
    public static synchronized void close() {
        Playwright playwright = THREAD_PLAYWRIGHT.get();
        if (playwright != null) {
            try {
                playwright.close();
            } catch (Exception e) {
                log.warn("[{}] Failed to close Playwright cleanly: {}", Thread.currentThread().getName(), e.getMessage());
            } finally {
                THREAD_PLAYWRIGHT.remove();
            }
        }
    }
}


