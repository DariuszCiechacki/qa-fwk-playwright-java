package io.github.qa.junit;

import io.github.qa.playwright.config.debugging.DebuggingConfigResolver;
import io.github.qa.playwright.debugging.DebuggingManager;
import io.github.qa.playwright.session.PlaywrightSessionManager;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PlaywrightExtension
        implements BeforeEachCallback, AfterTestExecutionCallback, AfterEachCallback, AfterAllCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        PlaywrightSessionManager.startPlaywrightSession();
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isEmpty()) {
            return;
        }

        if (!DebuggingConfigResolver.isScreenshotsOnFailureEnabled()) {
            return;
        }

        DebuggingManager.captureFailureArtifacts(context);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        DebuggingManager.cleanUp(context);
        PlaywrightSessionManager.getCurrentSession().closeSession();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        PlaywrightSessionManager.cleanUp();
    }
}
