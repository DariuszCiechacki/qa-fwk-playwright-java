package io.github.qa.playwright.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.qa.exception.ConfigurationLoadException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * Loads and provides access to the {@link PlaywrightConfig} instance.
 * Implements thread-safe lazy initialization.
 */
@Slf4j
@Getter
public class PlaywrightConfigProvider {

    private static final String PLAYWRIGHT_CONFIG_FILE = "playwright-config.yml";
    private final PlaywrightConfig config;

    private PlaywrightConfigProvider() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PLAYWRIGHT_CONFIG_FILE)) {
            if (input == null) {
                throw new ConfigurationLoadException("Configuration file not found: " + PLAYWRIGHT_CONFIG_FILE, null
                );
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            this.config = mapper.readValue(input, PlaywrightConfig.class);
        } catch (Exception e) {
            throw new ConfigurationLoadException("Failed to load Playwright configuration.", e
            );
        }
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
