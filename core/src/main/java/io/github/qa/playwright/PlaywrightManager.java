package io.github.qa.playwright;

import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;

/**
 * Manages lifecycle of a single global {@link Playwright} instance.
 * <p>
 * Created once for the entire test run and closed safely after all tests.
 * </p>
 */
@Slf4j
public final class PlaywrightManager {

    private static final ThreadLocal<Playwright> THREAD_PLAYWRIGHT =
            ThreadLocal.withInitial(() -> {
                Playwright pw = Playwright.create();
                log.info("[{}] Playwright instance created.", Thread.currentThread().getName());
                return pw;
            });

    private PlaywrightManager() {
    }

    /**
     * Returns Playwright for the current test thread.
     */
    public static Playwright getCurrentInstance() {
        return THREAD_PLAYWRIGHT.get();
    }

    /**
     * Closes Playwright for the current thread.
     */
    public static void close() {
        Playwright pw = THREAD_PLAYWRIGHT.get();
        if (pw != null) {
            try {
                pw.close();
                log.info("[{}] Playwright closed.", Thread.currentThread().getName());
            } catch (Exception e) {
                log.warn("[{}] Failed to close Playwright cleanly: {}", Thread.currentThread().getName(), e.getMessage());
            } finally {
                THREAD_PLAYWRIGHT.remove();
            }
        }
    }
}

