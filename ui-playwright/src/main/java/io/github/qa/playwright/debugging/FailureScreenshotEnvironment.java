package io.github.qa.playwright.debugging;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
final class FailureScreenshotEnvironment {

    private static final Path SCREENSHOT_DIRECTORY = Paths.get("target", "screenshots", "failures");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS");

    public Optional<Path> prepareTargetPath(ExtensionContext context) {
        try {
            Files.createDirectories(SCREENSHOT_DIRECTORY);
            return Optional.of(SCREENSHOT_DIRECTORY.resolve(buildFileName(context)));
        } catch (Exception exception) {
            log.warn("Failed to prepare failure screenshot output directory {}.", SCREENSHOT_DIRECTORY, exception);
            return Optional.empty();
        }
    }

    private String buildFileName(ExtensionContext context) {
        String className = context.getTestClass()
                .map(Class::getSimpleName)
                .orElse("unknown-class");
        String methodName = context.getTestMethod()
                .map(Method::getName)
                .orElse("unknown-method");
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String uniqueId = Integer.toHexString(context.getUniqueId().hashCode());

        return sanitize(className)
                + "-"
                + sanitize(methodName)
                + "-"
                + sanitize(uniqueId)
                + "-"
                + timestamp
                + ".png";
    }

    private String sanitize(String value) {
        return value == null ? "unknown" : value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
