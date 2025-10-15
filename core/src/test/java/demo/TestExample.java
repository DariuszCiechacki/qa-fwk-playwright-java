package demo;

import com.microsoft.playwright.Page;
import io.github.qa.config.PlaywrightConfigLoader;
import io.github.qa.playwright.page.PageFactory;

public class TestExample {
    public static void main(String[] args) {
        var config = PlaywrightConfigLoader.get().getConfig();
        System.out.println("=== Framework Configuration ===");
        System.out.println("Browser type: " + config.getBrowserConfig().getType());
        System.out.println("Headless: " + config.getBrowserConfig().isHeadless());
        System.out.println("Locale: " + config.getContextConfig().getLocale());
        System.out.println("Default timeout: " + config.getPageConfig().getDefaultTimeout());
        System.out.println("Screenshot on failure: " + config.getDebuggingConfig().isScreenshotsOnFailure());

        Page page = PageFactory.createPage();
        page.navigate("https://google.pl");
        System.out.println("Page title: " + page.title());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }
}
