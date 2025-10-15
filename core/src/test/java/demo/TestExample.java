package demo;

import io.github.qa.config.PlaywrightConfigLoader;

public class TestExample {
    public static void main(String[] args) {
        var config = PlaywrightConfigLoader.get().getConfig();
        System.out.println("=== Framework Configuration ===");
        System.out.println("Browser type: " + config.getBrowserConfig().getType());
        System.out.println("Headless: " + config.getBrowserConfig().isHeadless());
        System.out.println("Locale: " + config.getContextConfig().getLocale());
        System.out.println("Default timeout: " + config.getPageConfig().getDefaultTimeout());
        System.out.println("Screenshot on failure: " + config.getDebuggingConfig().isScreenshotsOnFailure());
    }
}
