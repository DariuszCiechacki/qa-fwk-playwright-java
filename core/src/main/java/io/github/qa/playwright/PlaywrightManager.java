package io.github.qa.playwright;

import com.microsoft.playwright.Playwright;
import io.github.qa.exception.PlaywrightInitializationException;
import lombok.extern.slf4j.Slf4j;

/**
 * Manages lifecycle of a single {@link com.microsoft.playwright.Playwright} instance.
 * <p>
 * Ensures that Playwright is created once and closed safely at the end of execution.
 * Intended to be used by factories such as {@code BrowserFactory}, {@code ContextFactory} and {@code PageFactory}.
 * </p>
 */
@Slf4j
public final class PlaywrightManager {

    /**
     * Singleton Playwright instance.
     */
    private static Playwright playwrightInstance;

    private PlaywrightManager() {
        // Utility class; prevent instantiation.
    }

    /**
     * Returns the active {@link Playwright} instance, creating it if necessary.
     *
     * @return singleton Playwright instance
     * @throws PlaywrightInitializationException if initialization fails
     */
    public static synchronized Playwright getInstance() {
        if (playwrightInstance == null) {
            try {
                playwrightInstance = Playwright.create();
                log.info("[INFO] Playwright initialized successfully.");
            } catch (Exception e) {
                throw new PlaywrightInitializationException(e);
            }
        }
        return playwrightInstance;
    }

    /**
     * Closes the current Playwright instance safely, if active.
     */
    public static synchronized void close() {
        if (playwrightInstance != null) {
            try {
                playwrightInstance.close();
                log.info("[INFO] Playwright instance closed successfully.");
            } catch (Exception e) {
                log.warn("[WARN] Failed to close Playwright cleanly: {}", e.getMessage(), e);
            } finally {
                playwrightInstance = null;
            }
        }
    }
}
