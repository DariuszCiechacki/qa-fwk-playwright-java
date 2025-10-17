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
 * Factory responsible for creating and managing a single global {@link Browser} instance.
 * <p>
 * The Browser is launched once from the global {@link Playwright} instance
 * and safely shared across all test threads. Each test creates its own
 * {@link com.microsoft.playwright.BrowserContext} for full isolation.
 * </p>
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
     * Browser per test thread
     */
    private static final ThreadLocal<Browser> THREAD_BROWSER = new ThreadLocal<>();

    private BrowserFactory() {
    }

    public static Browser getBrowser() {
        Browser browser = THREAD_BROWSER.get();
        if (browser == null) {
            browser = createBrowser();
            THREAD_BROWSER.set(browser);
        }
        return browser;
    }

    private static Browser createBrowser() {
        try {
            Playwright playwright = PlaywrightManager.getCurrentInstance();
            BrowserConfig config = PlaywrightConfigProvider.get().getConfig().getBrowserConfig();

            String type = BrowserTypeName.getByName(config.getType());
            BrowserType browserType = BROWSER_TYPE_RESOLVERS.get(type).resolve(playwright);

            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                    .setHeadless(config.isHeadless())
                    .setSlowMo(config.getSlowMo());

            if (BrowserTypeName.CHROMIUM.getValue().equals(type)) {
                String channel = config.getChannel();
                if (channel != null && !channel.isBlank()) {
                    String resolved = ChromiumChannelName.getByName(channel);
                    CHANNEL_RESOLVERS.get(resolved).applyChannel(options);
                }
            }

            Browser browser = browserType.launch(options);
            log.info("[{}] Browser instance created (type={}, headless={})",
                    Thread.currentThread().getName(), type, config.isHeadless());
            return browser;

        } catch (Exception e) {
            throw new BrowserInitializationException(e);
        }
    }

    public static void closeBrowser() {
        Browser browser = THREAD_BROWSER.get();
        if (browser != null) {
            try {
                browser.close();
                log.info("[{}] Browser closed.", Thread.currentThread().getName());
            } catch (Exception e) {
                log.warn("[{}] Failed to close Browser cleanly: {}", Thread.currentThread().getName(), e.getMessage());
            } finally {
                THREAD_BROWSER.remove();
            }
        }
    }
}

