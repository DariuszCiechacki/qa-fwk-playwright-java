package io.github.qa.playwright.config;

import io.github.qa.core.config.YamlResourceLoader;
import lombok.Getter;

/**
 * Loads and provides access to the {@link PlaywrightConfig} instance.
 * Implements thread-safe lazy initialization.
 */
@Getter
public class PlaywrightConfigProvider {

    private static final String PLAYWRIGHT_CONFIG_FILE = "playwright-config.yml";
    private final PlaywrightConfig config;

    private PlaywrightConfigProvider() {
        this.config = YamlResourceLoader.load(PLAYWRIGHT_CONFIG_FILE, PlaywrightConfig.class);
    }

    /**
     * Returns the singleton loader instance.
     */
    public static PlaywrightConfigProvider get() {
        return LazyConfigLoader.INSTANCE;
    }

    /**
     * LazyConfigLoader for lazy, thread-safe singleton initialization.
     */
    private static class LazyConfigLoader {
        private static final PlaywrightConfigProvider INSTANCE = new PlaywrightConfigProvider();
    }
}
