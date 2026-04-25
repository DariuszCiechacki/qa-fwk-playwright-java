# QA Framework – Java

Modular Maven test automation framework built for multiple test types.
The current UI module is based on Playwright, with the shared `core` module reserved for cross-cutting capabilities that can be reused by UI, API, integration, and component testing.

## Tech Stack
- **Java:** 25
- **Playwright:** 1.55.0
- **Maven**
- **GitHub Actions / GitHub Packages**
- **Allure Reports**

## Failure Screenshots

Enable automatic screenshot capture for failed UI tests in `playwright-config.yml`:

```yaml
debuggingConfig:
  screenshotsOnFailure: true
```

When enabled, the framework stores screenshots under `target/screenshots/failures`.
