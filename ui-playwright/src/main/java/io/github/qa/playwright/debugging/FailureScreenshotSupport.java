package io.github.qa.playwright.debugging;

import com.microsoft.playwright.Page;
import io.github.qa.playwright.config.PlaywrightConfig;
import io.github.qa.playwright.config.PlaywrightConfigProvider;
import io.github.qa.playwright.config.debugging.DebuggingConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Captures browser screenshots for failed tests when enabled in the Playwright configuration.
 */
@Slf4j
public final class FailureScreenshotSupport {

    private static final Path SCREENSHOT_DIRECTORY = Paths.get("target", "screenshots", "failures");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS");

    private FailureScreenshotSupport() {
    }

    /**
     * Captures a screenshot for the current test when screenshot-on-failure is enabled.
     *
     * @return {@code true} if a screenshot was saved, otherwise {@code false}.
     */
    public static boolean captureIfEnabled(ExtensionContext context, Page page) {
        if (!isScreenshotOnFailureEnabled() || page == null) {
            return false;
        }

        try {
            Files.createDirectories(SCREENSHOT_DIRECTORY);
            Path screenshotPath = SCREENSHOT_DIRECTORY.resolve(buildFileName(context));
            page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));
            log.info("Failure screenshot saved to {}", screenshotPath.toAbsolutePath());
            return true;
        } catch (Exception exception) {
            log.warn("Failed to capture a screenshot for '{}'.", context.getUniqueId(), exception);
            return false;
        }
    }

    private static boolean isScreenshotOnFailureEnabled() {
        PlaywrightConfig config = PlaywrightConfigProvider.get().getConfig();
        if (config == null) {
            return false;
        }

        DebuggingConfig debuggingConfig = config.getDebuggingConfig();
        return debuggingConfig != null && debuggingConfig.isScreenshotsOnFailure();
    }

    private static String buildFileName(ExtensionContext context) {
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

    private static String sanitize(String value) {
        return Optional.ofNullable(value)
                .orElse("unknown")
                .replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
