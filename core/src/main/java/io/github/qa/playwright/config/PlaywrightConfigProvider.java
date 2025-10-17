package io.github.qa.playwright.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.qa.exception.ConfigurationLoadException;
import java.io.InputStream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Loads and provides access to the {@link PlaywrightConfig} instance.
 * <p>Implements thread-safe lazy initialization using the static-holder idiom.</p>
 */
@Slf4j
@Getter
public class PlaywrightConfigProvider {
    private static final String CONFIG_FILE = "playwright-config.yml";
    private final PlaywrightConfig config;

    private PlaywrightConfigProvider() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new ConfigurationLoadException("Configuration file not found: " + CONFIG_FILE, null
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
