package io.github.qa.junit;

import io.github.qa.playwright.session.PlaywrightSessionManager;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PlaywrightExtension implements BeforeEachCallback, AfterEachCallback, AfterAllCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        PlaywrightSessionManager.startPlaywrightSession();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        PlaywrightSessionManager.getCurrentSession().closeSession();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        PlaywrightSessionManager.cleanUp();
    }
}
