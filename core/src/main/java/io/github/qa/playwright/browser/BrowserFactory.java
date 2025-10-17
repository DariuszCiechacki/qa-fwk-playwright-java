package io.github.qa.playwright.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import io.github.qa.exception.BrowserInitializationException;
import io.github.qa.playwright.PlaywrightManager;
import io.github.qa.playwright.browser.channel.ChromeChannelResolver;
import io.github.qa.playwright.browser.channel.ChromiumChannelName;
import io.github.qa.playwright.browser.channel.ChromiumChannelResolver;
import io.github.qa.playwright.browser.channel.EdgeChannelResolver;
import io.github.qa.playwright.browser.type.*;
import io.github.qa.playwright.config.PlaywrightConfigProvider;
import io.github.qa.playwright.config.browser.BrowserConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Factory responsible for creating and managing a single {@link Browser} instance.
 * Uses browser-type resolvers to obtain the correct {@link BrowserType}, then applies launch options defined in the configuration.
 */
@Slf4j
public final class BrowserFactory {

    private static final Map<String, BrowserTypeResolver> BROWSER_TYPE_RESOLVERS = Map.of(
            BrowserTypeName.CHROMIUM.getValue(), new ChromiumResolver(),
            BrowserTypeName.FIREFOX.getValue(), new FirefoxResolver(),
            BrowserTypeName.WEBKIT.getValue(), new WebkitResolver()
    );
    private static final Map<String, ChromiumChannelResolver> CHANNEL_RESOLVERS = Map.of(
            ChromiumChannelName.CHROME.getValue(), new ChromeChannelResolver(),
            ChromiumChannelName.MSEDGE.getValue(), new EdgeChannelResolver()
    );

    private static Browser browserInstance;

    private BrowserFactory() {
    }

    /**
     * Returns a singleton {@link Browser} instance created according to configuration.
     *
     * @return active Playwright {@link Browser}.
     * @throws BrowserInitializationException if browser creation fails.
     */
    public static synchronized Browser getBrowser() {
        if (browserInstance == null) {
            try {
                // Ensure Playwright is initialized
                Playwright playwright = PlaywrightManager.getInstance();
                // Load browser configuration
                BrowserConfig browserConfig = PlaywrightConfigProvider.get().getConfig().getBrowserConfig();

                // Resolve the appropriate BrowserType
                String type = BrowserTypeName.getByName(browserConfig.getType());
                BrowserType browserType = BROWSER_TYPE_RESOLVERS.get(type).resolve(playwright);
                log.info("Creating browser of type: {}", type);

                // Set up browser launch options
                BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                        .setHeadless(browserConfig.isHeadless())
                        .setSlowMo(browserConfig.getSlowMo());

                // Apply channel only for Chromium-based browsers
                if (BrowserTypeName.CHROMIUM.getValue().equals(type)) {
                    if (browserConfig.getChannel() == null || browserConfig.getChannel().isBlank()) {
                        log.info("No browser channel specified; using default for type: {}", type);
                    } else {
                        String channel = ChromiumChannelName.getByName(browserConfig.getChannel());
                        ChromiumChannelResolver channelResolver = CHANNEL_RESOLVERS.get(channel);
                        channelResolver.applyChannel(options);
                        log.info("Using Chromium channel: {}", channel);
                    }
                }
                browserInstance = browserType.launch(options);
            } catch (Exception e) {
                throw new BrowserInitializationException(e);
            }
        }
        return browserInstance;
    }

    public static synchronized void reset() {
        browserInstance = null;
    }
}
