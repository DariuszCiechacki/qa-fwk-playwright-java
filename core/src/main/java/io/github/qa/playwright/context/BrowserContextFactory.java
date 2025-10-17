package io.github.qa.playwright.context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import io.github.qa.exception.ContextInitializationException;
import io.github.qa.playwright.browser.BrowserFactory;
import io.github.qa.playwright.config.PlaywrightConfigProvider;
import io.github.qa.playwright.config.context.ContextConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory responsible for creating new isolated {@link BrowserContext} instances.
 * <p>
 * Each test gets its own context, created from the global shared {@link Browser}.
 * Contexts are independent, allowing full isolation between parallel tests.
 * </p>
 */
@Slf4j
public final class BrowserContextFactory {

    private BrowserContextFactory() {
    }

    public static BrowserContext createContext() {
        try {
            Browser browser = BrowserFactory.getBrowser();
            ContextConfig config = PlaywrightConfigProvider.get().getConfig().getContextConfig();

            Browser.NewContextOptions options = new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(config.isIgnoreHTTPSErrors())
                    .setLocale(config.getLocale());

            if (config.getViewport() != null) {
                options.setViewportSize(config.getViewport().getWidth(), config.getViewport().getHeight());
            }

            BrowserContext context = browser.newContext(options);
            log.info("[{}] BrowserContext created (locale={}, ignoreHTTPSErrors={})",
                    Thread.currentThread().getName(), config.getLocale(), config.isIgnoreHTTPSErrors());

            return context;
        } catch (Exception e) {
            throw new ContextInitializationException(e);
        }
    }

    public static void closeContext(BrowserContext context) {
        if (context != null) {
            try {
                context.close();
                log.info("[{}] BrowserContext closed.", Thread.currentThread().getName());
            } catch (Exception e) {
                log.warn("[{}] Failed to close BrowserContext cleanly: {}", Thread.currentThread().getName(), e.getMessage());
            }
        }
    }
}
