package io.github.qa.playwright.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import io.github.qa.config.PlaywrightConfigLoader;
import io.github.qa.exception.BrowserInitializationException;
import io.github.qa.playwright.PlaywrightManager;
import java.util.Map;

/**
 * Factory responsible for creating and managing a single {@link Browser} instance.
 * <p>
 * Uses browser-type resolvers to obtain the correct {@link BrowserType}, then applies
 * launch options defined in the configuration.
 * </p>
 */
public final class BrowserFactory {

    private static final Map<String, BrowserTypeResolver> RESOLVERS = Map.of(
        "chromium", new ChromiumResolver(),
        "firefox", new FirefoxResolver(),
        "webkit", new WebkitResolver()
    );

    private static Browser browserInstance;

    private BrowserFactory() {}

    /**
     * Returns a singleton {@link Browser} instance created according to configuration.
     *
     * @return active Playwright {@link Browser}
     * @throws BrowserInitializationException if browser creation fails
     */
    public static synchronized Browser getBrowser() {
        if (browserInstance == null) {
            try {
                // Ensure Playwright is initialized
                Playwright playwright = PlaywrightManager.getInstance();

                // Load browser configuration
                var browserConfig = PlaywrightConfigLoader.get().getConfig().getBrowserConfig();
                // Resolve the appropriate BrowserType
                String type = browserConfig.getType().toLowerCase();
                BrowserTypeResolver resolver = RESOLVERS.get(type);
                if (resolver == null) {
                    throw new IllegalArgumentException("Unsupported browser type: " + type);
                }
                BrowserType browserType = resolver.resolve(playwright);

                BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                    .setHeadless(browserConfig.isHeadless())
                    .setSlowMo(browserConfig.getSlowMo());

                // Chromium-based browsers can have a channel (e.g., "chrome", "msedge")
                if ("chromium".equals(type) && browserConfig.getChannel() != null) {
                    options.setChannel(browserConfig.getChannel());
                }

                browserInstance = browserType.launch(options);

            } catch (Exception e) {
                throw new BrowserInitializationException("Failed to create browser instance", e);
            }
        }
        return browserInstance;
    }
}
