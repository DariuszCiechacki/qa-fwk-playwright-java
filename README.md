# QA Framework Playwright Java

Java 25 Maven QA framework for Playwright-based UI test automation. The framework is designed to be published as Maven artifacts and consumed by external executable test projects.

Consumer projects should inherit the framework-provided `test-parent` POM and depend on `ui-playwright`. The parent POM supplies the executable test setup; the UI module supplies Playwright lifecycle, configuration, session access, and failure debugging support.

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Module Architecture](#module-architecture)
- [Requirements](#requirements)
- [Consume from GitHub Packages](#consume-from-github-packages)
- [Consumer Project Setup](#consumer-project-setup)
- [Playwright Configuration](#playwright-configuration)
- [Writing Tests](#writing-tests)
- [Allure Steps and Reports](#allure-steps-and-reports)
- [Failure Screenshots](#failure-screenshots)
- [Maintainer Commands](#maintainer-commands)
- [Publishing Notes](#publishing-notes)
- [Troubleshooting](#troubleshooting)
- [Current Limitations and Notes](#current-limitations-and-notes)

## Overview

`qa-fwk-playwright-java` provides reusable test infrastructure for Java UI automation:

- a published Maven parent for executable test projects: `io.github.dariuszciechacki:test-parent`
- a Playwright UI dependency for consumer tests: `io.github.dariuszciechacki:ui-playwright`
- framework-owned configuration loading from `playwright-config.yml`
- JUnit 5 extension-managed Playwright sessions
- Allure JUnit 5 and `@Step` support through the parent POM

The intended consumer model is:

1. Configure Maven access to GitHub Packages.
2. Create an external test project that inherits `test-parent`.
3. Add `ui-playwright` as a dependency.
4. Add `src/test/resources/playwright-config.yml`.
5. Register `PlaywrightExtension` in JUnit 5 tests.

## Key Features

- **Java 25 by design** — source, target, and compiler release are configured for Java 25.
- **Playwright Java 1.55.0** — browser automation through Microsoft Playwright for Java.
- **JUnit 5 lifecycle integration** — `PlaywrightExtension` starts one Playwright session before each test and closes it after each test.
- **Thread-local session management** — each test thread owns its own Playwright/browser session state.
- **Isolated browser contexts** — every test gets a new `BrowserContext` and `Page`.
- **YAML configuration** — browser, context, page, and debugging options are loaded from classpath resource `playwright-config.yml`.
- **Allure-ready test execution** — `test-parent` configures Allure JUnit 5, Allure Maven plugin, AspectJ Weaver, and Surefire `-javaagent` support.
- **Failure screenshots** — optional automatic screenshots on failed tests, saved under `target/screenshots/failures` and attached to Allure when Allure is present.
- **No forced logging backend** — the framework depends on `slf4j-api`; consumer projects may choose their own SLF4J implementation.

## Module Architecture

| Module | Artifact | Purpose | Consumer guidance |
| --- | --- | --- | --- |
| Root parent | `io.github.dariuszciechacki:parent` | Aggregates framework modules and centralizes dependency/plugin management. | Framework maintainers only; consumers normally inherit `test-parent`, not this root parent. |
| `test-parent` | `io.github.dariuszciechacki:test-parent` | Parent POM for executable test projects. Configures JUnit 5, Allure JUnit 5, AspectJ Weaver, Surefire `javaagent`, and Allure Maven plugin. | Consumer test projects should inherit this parent. |
| `core` | `io.github.dariuszciechacki:core` | Shared cross-cutting utilities, currently YAML resource loading. | Pulled transitively by `ui-playwright`. |
| `ui-playwright` | `io.github.dariuszciechacki:ui-playwright` | Playwright UI automation module: browser/context/page factories, config model, JUnit extension, session manager, and debugging artifacts. | Consumer test projects should add this dependency. |
| `report` | `io.github.dariuszciechacki:report` | Reporting-related module placeholder/artifacts. | Not required for normal consumers today; executable reporting setup currently lives in `test-parent`. |

## Requirements

- **JDK 25** installed and selected for Maven (`java -version` and `mvn -version`).
- **Maven 3.9+** recommended.
- Access to **GitHub Packages** for `DariuszCiechacki/qa-fwk-playwright-java`.
- Playwright browser binaries installed for the environment running tests.

Install Playwright browsers from a consumer project with the Playwright Java CLI after dependencies resolve:

```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

If the consumer project does not configure the Maven Exec Plugin, run the equivalent Playwright Java install command used by your project or CI image.

## Consume from GitHub Packages

Artifacts are published to GitHub Packages:

```text
https://maven.pkg.github.com/dariuszciechacki/qa-fwk-playwright-java
```

GitHub Packages requires credentials for dependency resolution. Add a server entry to `~/.m2/settings.xml`. Use a GitHub username and a token with package read access. Do not commit credentials.

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>${env.GITHUB_ACTOR}</username>
      <password>${env.GITHUB_TOKEN}</password>
    </server>
  </servers>

  <profiles>
    <profile>
      <id>github-packages</id>
      <repositories>
        <repository>
          <id>github</id>
          <url>https://maven.pkg.github.com/dariuszciechacki/qa-fwk-playwright-java</url>
        </repository>
      </repositories>
    </profile>
  </profiles>

  <activeProfiles>
    <activeProfile>github-packages</activeProfile>
  </activeProfiles>
</settings>
```

Use environment variables or your CI secret store for token values.

## Consumer Project Setup

Minimal external test project layout:

```text
my-ui-tests/
├── pom.xml
└── src/test/
    ├── java/
    │   └── com/example/tests/HomePageTest.java
    └── resources/
        └── playwright-config.yml
```

Example consumer `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>io.github.dariuszciechacki</groupId>
    <artifactId>test-parent</artifactId>
    <version>1.0-SNAPSHOT</version>
    <relativePath/>
  </parent>

  <groupId>com.example</groupId>
  <artifactId>my-ui-tests</artifactId>
  <version>1.0-SNAPSHOT</version>

  <dependencies>
    <dependency>
      <groupId>io.github.dariuszciechacki</groupId>
      <artifactId>ui-playwright</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>

    <!-- Optional: choose a logging backend for SLF4J output. -->
    <!--
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-simple</artifactId>
      <version>2.0.13</version>
      <scope>test</scope>
    </dependency>
    -->
  </dependencies>
</project>
```

Why inherit `test-parent`?

- It provides JUnit 5 API/engine dependencies.
- It provides Allure JUnit 5 integration.
- It configures AspectJ Weaver as a Surefire `-javaagent` so Allure `@Step` annotations work during Maven test execution.
- It provides the Allure Maven plugin for report generation.

Consumers should not duplicate the AspectJ/Surefire setup unless intentionally opting out of the framework parent.

## Playwright Configuration

Create `src/test/resources/playwright-config.yml` in the consumer test project. The file name is required; the framework loads exactly `playwright-config.yml` from the test runtime classpath.

```yaml
browserConfig:
  type: chromium      # chromium, firefox, or webkit
  channel: chrome     # chromium only: chrome or msedge; omit or use empty value for default bundled Chromium
  headless: true
  slowMo: 0

contextConfig:
  ignoreHTTPSErrors: false
  locale: en-US
  viewport:
    width: 1280
    height: 720

pageConfig:
  defaultTimeout: 30000
  navigationTimeout: 30000

debuggingConfig:
  screenshotsOnFailure: true
```

Notes:

- `browserConfig.type` is mandatory and must be one of `chromium`, `firefox`, or `webkit`.
- `browserConfig.channel` is applied only for Chromium. Supported channel values currently are `chrome` and `msedge`.
- Page timeouts are applied only when greater than `0`.
- Failure screenshots are disabled unless `debuggingConfig.screenshotsOnFailure` is `true`.

## Writing Tests

Register `PlaywrightExtension` and access the current page through `PlaywrightSessionManager`.

```java
package com.example.tests;

import com.microsoft.playwright.Page;
import io.github.qa.junit.PlaywrightExtension;
import io.github.qa.playwright.session.PlaywrightSessionManager;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(PlaywrightExtension.class)
class HomePageTest {

    @Test
    void opensExampleDomain() {
        Page page = PlaywrightSessionManager.getCurrentSession().getPage();

        open(page, "https://example.com");

        assertTrue(page.title().contains("Example Domain"));
    }

    @Step("Open {url}")
    void open(Page page, String url) {
        page.navigate(url);
    }
}
```

Lifecycle provided by `PlaywrightExtension`:

- `beforeEach` starts the Playwright stack for the current test.
- each test gets a fresh `BrowserContext` and `Page`.
- failed tests can trigger screenshot capture before cleanup.
- `afterEach` closes the test context/page.
- `afterAll` closes the thread-local browser and Playwright instance.

## Allure Steps and Reports

Allure support is available through `test-parent`:

- `allure-junit5` dependency
- `aspectjweaver` test dependency
- Maven Surefire `argLine` with AspectJ `-javaagent`
- `allure-maven` plugin

This is why consumers should inherit `test-parent`: Allure `@Step` annotations need AspectJ weaving during test execution. Without the parent POM, tests may still compile, but step annotations may not be represented correctly in Allure results unless the consumer duplicates the javaagent setup.

Run tests and generate/open an Allure report from the consumer project:

```bash
mvn test
mvn allure:report
mvn allure:serve
```

Allure result files are produced by the standard Allure JUnit 5 integration under the usual Maven test output locations.

## Failure Screenshots

Enable screenshots in `playwright-config.yml`:

```yaml
debuggingConfig:
  screenshotsOnFailure: true
```

When a JUnit test using `PlaywrightExtension` fails, the framework:

1. checks whether `screenshotsOnFailure` is enabled;
2. captures the current Playwright `Page`;
3. saves a PNG file under `target/screenshots/failures`;
4. attaches it to Allure as `Failure screenshot` when Allure is available on the runtime classpath.

Screenshot file names include the test class, method, JUnit unique id hash, and timestamp.

## Maintainer Commands

Run from this repository root.

Build all modules:

```bash
mvn clean verify
```

Run tests only:

```bash
mvn test
```

Install artifacts into the local Maven repository for local consumer testing:

```bash
mvn clean install
```

Build with a release-like revision value:

```bash
mvn clean verify -Drevision=1.0.0
```

## Publishing Notes

The root POM publishes to GitHub Packages using distribution management id `github`:

```text
https://maven.pkg.github.com/dariuszciechacki/qa-fwk-playwright-java
```

Maintainers need a matching `github` server in Maven settings with package write permissions.

Verified development/SNAPSHOT publish command using the project version currently defined by the POMs:

```bash
mvn clean deploy
```

Non-SNAPSHOT release publishing requires aligning the module and parent versions first. Do not rely on `-Drevision=...` alone until the release flow verifies that generated external POMs reference coherent non-SNAPSHOT parent/module versions.

## Troubleshooting

### Maven cannot resolve `test-parent`

- Confirm `~/.m2/settings.xml` has repository `https://maven.pkg.github.com/dariuszciechacki/qa-fwk-playwright-java`.
- Confirm server id `github` matches the repository id.
- Confirm the token has permission to read GitHub Packages.
- Keep `<relativePath/>` in the consumer parent declaration so Maven does not look for a local parent POM.

### Maven runs with the wrong Java version

Java 25 is required. Check:

```bash
java -version
mvn -version
```

Set `JAVA_HOME` to a JDK 25 installation before running Maven.

### `playwright-config.yml` is not found

- The file must be named exactly `playwright-config.yml`.
- Place it in `src/test/resources` so it is available on the test runtime classpath.
- Ensure the test resources are not excluded by the consumer build.

### Browser launch fails

- Install Playwright browser binaries for the environment.
- If using `channel: chrome` or `channel: msedge`, ensure that browser channel is installed on the host.
- For bundled Playwright Chromium, omit `channel` or leave it empty.

### Allure `@Step` annotations are missing

- Ensure the consumer POM inherits `io.github.dariuszciechacki:test-parent`.
- Run tests through Maven Surefire (`mvn test`) so the configured AspectJ `-javaagent` is used.
- If the consumer overrides Surefire `argLine`, preserve the parent-provided `@{argLine}` and AspectJ javaagent configuration.

### Failure screenshots are missing

- Confirm the test class uses `@ExtendWith(PlaywrightExtension.class)`.
- Confirm `debuggingConfig.screenshotsOnFailure: true`.
- Confirm the failure occurs after a Playwright page has been created.
- Check `target/screenshots/failures` in the consumer project.

### No framework logs are shown

The framework exposes `slf4j-api` but does not force a logging implementation. Add an SLF4J backend, such as `slf4j-simple`, Logback, or another implementation, in the consumer project if logs are needed.

## Current Limitations and Notes

- Java 25 is mandatory and intentional.
- The main consumer path is `test-parent` + `ui-playwright`.
- The `report` module exists for reporting-related artifacts, but executable Allure setup currently belongs to `test-parent`; consumers should not depend on `report` for normal UI tests unless future framework guidance changes.
- `playwright-config.yml` is currently a fixed classpath resource name.
- Supported browser types are `chromium`, `firefox`, and `webkit`.
- Supported Chromium channels are currently `chrome` and `msedge`; other channels are not exposed by the current resolver implementation.
- `slf4j-simple` is not forced by the framework; choose a logging backend in the consumer project if needed.
