package io.github.qa.junit;

import io.github.qa.playwright.debugging.FailureScreenshotSupport;
import io.github.qa.playwright.session.PlaywrightSessionManager;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PlaywrightExtension
        implements BeforeEachCallback, AfterTestExecutionCallback, AfterEachCallback, AfterAllCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(PlaywrightExtension.class);
    private static final String FAILURE_SCREENSHOT_CAPTURED_KEY = "failureScreenshotCaptured";

    @Override
    public void beforeEach(ExtensionContext context) {
        PlaywrightSessionManager.startPlaywrightSession();
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        captureFailureScreenshot(context);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        captureFailureScreenshot(context);
        PlaywrightSessionManager.getCurrentSession().closeSession();
        context.getStore(NAMESPACE).remove(FAILURE_SCREENSHOT_CAPTURED_KEY);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        PlaywrightSessionManager.cleanUp();
    }

    private void captureFailureScreenshot(ExtensionContext context) {
        if (context.getExecutionException().isEmpty()) {
            return;
        }

        if (Boolean.TRUE.equals(context.getStore(NAMESPACE).get(FAILURE_SCREENSHOT_CAPTURED_KEY, Boolean.class))) {
            return;
        }

        boolean captured = FailureScreenshotSupport.captureIfEnabled(
                context,
                PlaywrightSessionManager.getCurrentSession().getPage()
        );
        if (captured) {
            context.getStore(NAMESPACE).put(FAILURE_SCREENSHOT_CAPTURED_KEY, true);
        }
    }
}
