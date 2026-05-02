package io.github.qa.playwright.debugging;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
final class AllureAttachmentSupport {

    private static final String ALLURE_CLASS_NAME = "io.qameta.allure.Allure";
    private static final String ATTACHMENT_NAME = "Failure screenshot";
    private static final String CONTENT_TYPE = "image/png";
    private static final String FILE_EXTENSION = ".png";

    private AllureAttachmentSupport() {
    }

    static void attachFailureScreenshot(Path screenshotPath) {
        try (InputStream screenshot = Files.newInputStream(screenshotPath)) {
            Class<?> allure = Class.forName(ALLURE_CLASS_NAME);
            Method addAttachment = allure.getMethod(
                    "addAttachment",
                    String.class,
                    String.class,
                    InputStream.class,
                    String.class
            );

            addAttachment.invoke(null, ATTACHMENT_NAME, CONTENT_TYPE, screenshot, FILE_EXTENSION);
            log.debug("Failure screenshot attached to Allure from {}.", screenshotPath.toAbsolutePath());
        } catch (ClassNotFoundException exception) {
            log.debug("Allure is not available; skipping failure screenshot attachment.");
        } catch (Exception | LinkageError exception) {
            log.debug("Failed to attach failure screenshot to Allure from {}.", screenshotPath.toAbsolutePath(), exception);
        }
    }
}
