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
 * Factory responsible for creating {@link Browser} instances.
 * Each test thread holds its own Browser instance, created once per thread.
 * The Browser is launched from the {@link Playwright} instance managed by {@link PlaywrightManager}.
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

    /**
     * One Browser instance per test thread.
     */
    private static final ThreadLocal<Browser> THREAD_BROWSER = new ThreadLocal<>();

    private BrowserFactory() {
    }

    /**
     * Returns the Browser for the current thread, creating it if missing.
     */
    public static synchronized void initialize() {
        if (THREAD_BROWSER.get() == null) {
            Browser browser = createBrowser();
            THREAD_BROWSER.set(browser);
            log.info("[{}] Browser instance initialized.", Thread.currentThread().getName());
        } else {
            log.debug("[{}] Browser already initialized; skipping.", Thread.currentThread().getName());
        }
    }

    /**
     * Creates a new Browser instance based on the configuration.
     */
    private static Browser createBrowser() {
        try {
            // Ensure Playwright is initialized
            Playwright playwright = PlaywrightManager.getCurrentInstance();

            // Load browser configuration
            BrowserConfig browserConfig = PlaywrightConfigProvider.get().getConfig().getBrowserConfig();

            // Resolve the appropriate BrowserType
            String type = BrowserTypeName.getByName(browserConfig.getType());
            BrowserType browserType = BROWSER_TYPE_RESOLVERS.get(type).resolve(playwright);
            log.info("Creating browser of type: {}.", type);

            // Set up browser launch options
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                    .setHeadless(browserConfig.isHeadless())
                    .setSlowMo(browserConfig.getSlowMo());

            // Apply channel only for Chromium-based browsers
            if (BrowserTypeName.CHROMIUM.getValue().equals(type)) {
                if (browserConfig.getChannel() == null || browserConfig.getChannel().isBlank()) {
                    log.info("No browser channel specified; using default for type: {}.", type);
                } else {
                    String channel = ChromiumChannelName.getByName(browserConfig.getChannel());
                    ChromiumChannelResolver channelResolver = CHANNEL_RESOLVERS.get(channel);
                    channelResolver.applyChannel(options);
                    log.info("Using Chromium channel: {}.", channel);
                }
            }

            Browser browser = browserType.launch(options);
            log.info("[{}] Playwright Browser created.)", Thread.currentThread().getName());
            return browser;
        } catch (Exception e) {
            throw new BrowserInitializationException(e);
        }
    }

    /**
     * Returns the current {@link Browser} instance for this thread.
     *
     * @throws IllegalStateException if Browser has not been initialized.
     */
    public static Browser getCurrentInstance() {
        Browser browser = THREAD_BROWSER.get();
        if (browser == null) {
            throw new IllegalStateException(
                    "Browser instance not initialized. Call BrowserFactory.initialize() first.");
        }
        return browser;
    }

    /**
     * Closes and removes the Browser instance for the current thread, if active.
     */
    public static synchronized void close() {
        Browser browser = THREAD_BROWSER.get();
        if (browser != null) {
            try {
                browser.close();
                log.info("[{}] Browser instance closed successfully.", Thread.currentThread().getName());
            } catch (Exception e) {
                log.warn("[{}] Failed to close Browser cleanly: {}", Thread.currentThread().getName(), e.getMessage());
            } finally {
                THREAD_BROWSER.remove();
            }
        }
    }
}

