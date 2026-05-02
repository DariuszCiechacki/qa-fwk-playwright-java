package io.github.qa.playwright.debugging;

import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Central entry point for debugging artifacts captured during test execution.
 */
public final class DebuggingManager {

    private static final FailureScreenshotArtifact FAILURE_SCREENSHOT_ARTIFACT =
            new FailureScreenshotArtifact(new FailureScreenshotEnvironment());

    private DebuggingManager() {
    }

    public static void captureFailureArtifacts(ExtensionContext context) {
        FAILURE_SCREENSHOT_ARTIFACT.capture(context);
    }

    public static void cleanUp(ExtensionContext context) {
        FAILURE_SCREENSHOT_ARTIFACT.cleanUp(context);
    }
}
