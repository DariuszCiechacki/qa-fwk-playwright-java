package io.github.qa.playwright.context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import io.github.qa.config.ContextConfig;
import io.github.qa.config.PlaywrightConfigLoader;
import io.github.qa.exception.ContextInitializationException;
import io.github.qa.playwright.browser.BrowserFactory;

/**
 * Factory responsible for creating new {@link com.microsoft.playwright.BrowserContext} instances.
 * <p>
 * Each context is isolated and configured according to {@link io.github.qa.config.ContextConfig}
 * loaded from <code>playwright-config.yml</code>.
 * </p>
 */
public final class BrowserContextFactory {
    private BrowserContextFactory() {
        // utility class
    }

    /**
     * Creates a new, fully configured {@link com.microsoft.playwright.BrowserContext}.
     *
     * @return new BrowserContext ready for use
     * @throws io.github.qa.exception.ContextInitializationException if context creation fails
     */
    public static BrowserContext createContext() {
        try {
            // Obtain the browser instance from BrowserFactory
            Browser browser = BrowserFactory.getBrowser();
            // Load context configuration
            ContextConfig contextConfig = PlaywrightConfigLoader.get().getConfig().getContextConfig();

            // Configure context options based on loaded configuration
            Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setIgnoreHTTPSErrors(contextConfig.isIgnoreHTTPSErrors())
                .setLocale(contextConfig.getLocale());

            // Set viewport size if specified. Playwright provides a default 1280×720 if not set.
            if (contextConfig.getViewport() != null) {
                options.setViewportSize(
                    contextConfig.getViewport().getWidth(),
                    contextConfig.getViewport().getHeight()
                );
            }

            return browser.newContext(options);

        } catch (Exception e) {
            throw new ContextInitializationException("Failed to create browser context", e);
        }
    }
}
