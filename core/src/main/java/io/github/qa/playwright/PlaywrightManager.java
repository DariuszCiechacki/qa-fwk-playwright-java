package io.github.qa.playwright;

import com.microsoft.playwright.Playwright;
import io.github.qa.exception.PlaywrightInitializationException;

/**
 * Manages lifecycle of a single {@link com.microsoft.playwright.Playwright} instance.
 * <p>
 * Ensures that Playwright is created once and closed safely at the end of execution.
 * Intended to be used by factories such as {@code BrowserFactory}, {@code ContextFactory} and {@code PageFactory}.
 * </p>
 */
public final class PlaywrightManager {
    /** Singleton Playwright instance. */
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
            } catch (Exception e) {
                throw new PlaywrightInitializationException("Failed to initialize Playwright", e);
            }
        }
        return playwrightInstance;
    }
}
