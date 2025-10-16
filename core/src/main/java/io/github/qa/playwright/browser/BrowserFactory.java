package io.github.qa.playwright.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import io.github.qa.config.PlaywrightConfigLoader;
import io.github.qa.exception.BrowserInitializationException;
import io.github.qa.playwright.PlaywrightManager;
import io.github.qa.playwright.browser.channel.ChromeChannelResolver;
import io.github.qa.playwright.browser.channel.ChromiumChannelResolver;
import io.github.qa.playwright.browser.channel.EdgeChannelResolver;
import io.github.qa.playwright.browser.type.BrowserTypeResolver;
import io.github.qa.playwright.browser.type.ChromiumResolver;
import io.github.qa.playwright.browser.type.FirefoxResolver;
import io.github.qa.playwright.browser.type.WebkitResolver;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory responsible for creating and managing a single {@link Browser} instance.
 * <p>
 * Uses browser-type resolvers to obtain the correct {@link BrowserType}, then applies
 * launch options defined in the configuration.
 * </p>
 */
@Slf4j
public final class BrowserFactory {
    private static final Map<String, BrowserTypeResolver> BROWSER_TYPE_RESOLVERS = Map.of(
        "chromium", new ChromiumResolver(),
        "firefox", new FirefoxResolver(),
        "webkit", new WebkitResolver()
    );
    private static final Map<String, ChromiumChannelResolver> CHANNEL_RESOLVERS = Map.of(
        "chrome", new ChromeChannelResolver(),
        "msedge", new EdgeChannelResolver()
    );

    private static Browser browserInstance;

    private BrowserFactory() {
    }

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
                BrowserTypeResolver resolver = BROWSER_TYPE_RESOLVERS.get(type);
                if (resolver == null) {
                    throw new IllegalArgumentException("Unsupported browser type: " + type);
                }
                BrowserType browserType = resolver.resolve(playwright);
                log.info("[INFO] Creating browser of type: {}", type);

                BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                    .setHeadless(browserConfig.isHeadless())
                    .setSlowMo(browserConfig.getSlowMo());

                // Apply channel only for Chromium-based browsers
                if ("chromium".equals(type)) {
                    String channel = browserConfig.getChannel();
                    if (channel != null && !channel.isBlank()) {
                        channel = channel.trim().toLowerCase();
                        ChromiumChannelResolver channelResolver = CHANNEL_RESOLVERS.get(channel);
                        if (channelResolver != null) {
                            channelResolver.applyChannel(options);
                            log.info("[INFO] Using Chromium channel: {}", channel);
                        } else {
                            log.warn("[WARN] Unknown Chromium channel '{}'; using default Chromium.", channel);
                        }
                    } else {
                        log.info("[INFO] No Chromium channel specified; using default Chromium.");
                    }
                }

                browserInstance = browserType.launch(options);
            } catch (Exception e) {
                throw new BrowserInitializationException("Failed to create browser instance", e);
            }
        }
        return browserInstance;
    }
}
