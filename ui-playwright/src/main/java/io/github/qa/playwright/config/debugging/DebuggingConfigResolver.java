package io.github.qa.playwright.config.debugging;

import io.github.qa.playwright.config.PlaywrightConfigProvider;

public final class DebuggingConfigResolver {

    private DebuggingConfigResolver() {
    }

    public static boolean isScreenshotsOnFailureEnabled() {
        try {
            DebuggingConfig debuggingConfig = PlaywrightConfigProvider.get().getConfig().getDebuggingConfig();
            return debuggingConfig != null && debuggingConfig.isScreenshotsOnFailure();
        } catch (RuntimeException ignored) {
            // Debug artifact capture should never mask the original test failure.
            return false;
        }
    }
}
