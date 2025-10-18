package demo;

import com.microsoft.playwright.Page;
import io.github.qa.junit.PlaywrightExtension;
import io.github.qa.playwright.session.PlaywrightSessionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(PlaywrightExtension.class)
public class TestExample2 {
    public static void main(String[] args) {
    }

    @Test
    void testExample1() {
        Page page = PlaywrightSessionManager.getCurrentSession().getPage();
        page.navigate("https://google.pl");
        System.out.println("Page title: " + page.title());
        Assertions.assertTrue(page.title().contains("Google"));
    }

    @Test
    void testExample2() {
        Page page = PlaywrightSessionManager.getCurrentSession().getPage();
        page.navigate("https://google.pl");
        System.out.println("Page title: " + page.title());
        Assertions.assertTrue(page.title().contains("Google"));
    }

    @Test
    void testExample3() {
        Page page = PlaywrightSessionManager.getCurrentSession().getPage();
        page.navigate("https://google.pl");
        System.out.println("Page title: " + page.title());
        Assertions.assertTrue(page.title().contains("Google"));
    }

    @Test
    void testExample4() {
        Page page = PlaywrightSessionManager.getCurrentSession().getPage();
        page.navigate("https://google.pl");
        System.out.println("Page title: " + page.title());
        Assertions.assertTrue(page.title().contains("Google"));
    }

    @Test
    void testExample5() {
        Page page = PlaywrightSessionManager.getCurrentSession().getPage();
        page.navigate("https://google.pl");
        System.out.println("Page title: " + page.title());
        Assertions.assertTrue(page.title().contains("Google"));
    }

    @Test
    void testExample6() {
        Page page = PlaywrightSessionManager.getCurrentSession().getPage();
        page.navigate("https://google.pl");
        System.out.println("Page title: " + page.title());
        Assertions.assertTrue(page.title().contains("Google"));
    }
}
