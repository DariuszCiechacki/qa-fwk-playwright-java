package io.github.qa.playwright.context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import io.github.qa.exception.ContextInitializationException;
import io.github.qa.playwright.browser.BrowserFactory;
import io.github.qa.playwright.config.PlaywrightConfigProvider;
import io.github.qa.playwright.config.context.ContextConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory responsible for creating isolated {@link BrowserContext} instances.
 * Each test gets its own independent context created from the
 * Browser managed by {@link BrowserFactory}.
 */
@Slf4j
public final class BrowserContextFactory {

    private BrowserContextFactory() {
    }

    /**
     * Creates a new, fully configured {@link BrowserContext}.
     *
     * @return new BrowserContext ready for use.
     * @throws ContextInitializationException if context creation fails.
     */
    public static BrowserContext createContext() {
        try {
            // Ensure Browser is initialized
            Browser browser = BrowserFactory.getCurrentInstance();
            // Load context configuration
            ContextConfig config = PlaywrightConfigProvider.get().getConfig().getContextConfig();

            // Configure context options based on loaded configuration
            Browser.NewContextOptions options = new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(config.isIgnoreHTTPSErrors())
                    .setLocale(config.getLocale());

            // Set viewport size if specified. Playwright provides a default 1280×720 if not set.
            if (config.getViewport() != null) {
                options.setViewportSize(
                        config.getViewport().getWidth(),
                        config.getViewport().getHeight()
                );
            }

            return browser.newContext(options);
        } catch (Exception e) {
            throw new ContextInitializationException(e);
        }
    }
}
