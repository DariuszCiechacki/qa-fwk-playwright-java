package demo;

import com.microsoft.playwright.Page;
import io.github.qa.junit.PlaywrightExtension;
import io.github.qa.playwright.config.PlaywrightConfigProvider;
import io.github.qa.playwright.session.PlaywrightSessionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(PlaywrightExtension.class)
public class TestExample {
    public static void main(String[] args) {
        var config = PlaywrightConfigProvider.get().getConfig();
        System.out.println("=== Framework Configuration ===");
        System.out.println("Browser type: " + config.getBrowserConfig().getType());
        System.out.println("Browser type: " + config.getBrowserConfig().getChannel());
        System.out.println("Headless: " + config.getBrowserConfig().isHeadless());
        System.out.println("Locale: " + config.getContextConfig().getLocale());
        System.out.println("Default timeout: " + config.getPageConfig().getDefaultTimeout());
        System.out.println("Screenshot on failure: " + config.getDebuggingConfig().isScreenshotsOnFailure());
    }

    @Test
    void testExample1() {
        Page page = PlaywrightSessionManager.getCurrentSession().getCurrentSessionPage();
        page.navigate("https://google.pl");
        System.out.println("Page title: " + page.title());
        Assertions.assertTrue(page.title().contains("Google"));
    }

    @Test
    void testExample2() {
        Page page = PlaywrightSessionManager.getCurrentSession().getCurrentSessionPage();
        page.navigate("https://google.pl");
        System.out.println("Page title: " + page.title());
        Assertions.assertTrue(page.title().contains("Google"));
    }

    @Test
    void testExample3() {
        Page page = PlaywrightSessionManager.getCurrentSession().getCurrentSessionPage();
        page.navigate("https://google.pl");
        System.out.println("Page title: " + page.title());
        Assertions.assertTrue(page.title().contains("Google"));
    }
}
