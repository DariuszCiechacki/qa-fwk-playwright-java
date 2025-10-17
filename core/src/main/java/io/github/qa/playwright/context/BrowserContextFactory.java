package io.github.qa.playwright.context;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import io.github.qa.exception.ContextInitializationException;
import io.github.qa.playwright.config.PlaywrightConfigProvider;
import io.github.qa.playwright.config.context.ContextConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory responsible for creating new {@link BrowserContext} instances.
 * Each context is isolated and configured according to {@link ContextConfig}
 * loaded from <code>playwright-config.yml</code>.
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
    public static BrowserContext createContext(Browser browser) {
        try {
            // Load context configuration
            ContextConfig contextConfig = PlaywrightConfigProvider.get().getConfig().getContextConfig();

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
            throw new ContextInitializationException(e);
        }
    }
}
