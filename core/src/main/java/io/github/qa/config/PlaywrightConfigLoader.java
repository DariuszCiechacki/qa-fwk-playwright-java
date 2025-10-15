package io.github.qa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.qa.config.exception.ConfigurationLoadException;
import java.io.InputStream;
import lombok.Getter;

/**
 * Loads and provides access to the {@link PlaywrightConfig} instance.
 * <p>Implements thread-safe lazy initialization using the static-holder idiom.</p>
 */
@Getter
public class PlaywrightConfigLoader {
    private final PlaywrightConfig config;

    private PlaywrightConfigLoader() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("playwright-config.yml")) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            this.config = mapper.readValue(input, PlaywrightConfig.class);
        } catch (Exception e) {
            throw new ConfigurationLoadException("Failed to load Playwright configuration", e);
        }
    }

    /** Holder for lazy, thread-safe singleton initialization. */
    private static class Holder {
        private static final PlaywrightConfigLoader INSTANCE = new PlaywrightConfigLoader();
    }

    /** Returns the singleton loader instance. */
    public static PlaywrightConfigLoader get() {
        return Holder.INSTANCE;
    }
}
