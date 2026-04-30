package io.github.qa.playwright.debugging;

import com.microsoft.playwright.Page;
import io.github.qa.playwright.session.PlaywrightSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.file.Path;
import java.util.Optional;

@Slf4j
final class FailureScreenshotArtifact {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(DebuggingManager.class, FailureScreenshotArtifact.class);
    private static final String SCREENSHOT_CAPTURED_KEY = "failureScreenshotCaptured";

    private final FailureScreenshotEnvironment environment;

    FailureScreenshotArtifact(FailureScreenshotEnvironment environment) {
        this.environment = environment;
    }

    public void capture(ExtensionContext context) {
        Page page = PlaywrightSessionManager.getCurrentSession().getPage();
        Optional<Path> screenshotPath = environment.prepareTargetPath(context);
        if (screenshotPath.isEmpty()) {
            return;
        }

        if (capture(page, screenshotPath.get())) {
            context.getStore(NAMESPACE).put(SCREENSHOT_CAPTURED_KEY, true);
        }
    }

    public void cleanUp(ExtensionContext context) {
        context.getStore(NAMESPACE).remove(SCREENSHOT_CAPTURED_KEY);
    }

    private boolean capture(Page page, Path screenshotPath) {
        try {
            page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));
            log.info("Failure screenshot saved to {}", screenshotPath.toAbsolutePath());
            return true;
        } catch (Exception exception) {
            log.warn("Failed to capture failure screenshot at {}.", screenshotPath.toAbsolutePath(), exception);
            return false;
        }
    }
}
