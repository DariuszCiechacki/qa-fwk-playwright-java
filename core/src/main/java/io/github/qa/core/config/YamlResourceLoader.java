package io.github.qa.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.qa.core.exception.ConfigurationLoadException;

import java.io.InputStream;

/**
 * Loads YAML-backed configuration objects from classpath resources.
 */
public final class YamlResourceLoader {

    private YamlResourceLoader() {
    }

    public static <T> T load(String resourceName, Class<T> targetType) {
        try (InputStream input = YamlResourceLoader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new ConfigurationLoadException("Configuration file not found: " + resourceName, null);
            }

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(input, targetType);
        } catch (ConfigurationLoadException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ConfigurationLoadException("Failed to load configuration from: " + resourceName, exception);
        }
    }
}
