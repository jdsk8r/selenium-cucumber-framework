# selenium-cucumber-framework

A lightweight framework that wires together [Selenium 4](https://www.selenium.dev/) and [Cucumber 7](https://cucumber.io/)
for writing BDD-style browser tests, from either **Java** or **Kotlin**.

It takes care of the repetitive plumbing you'd otherwise rewrite in every test project:
driver lifecycle (local or remote/Selenium Grid), sensible waits, screenshot-on-failure,
and small helpers around common `WebElement` interactions.

Example project: https://github.com/jdsk8r/example-selenium4-cucumber

Template: https://github.com/jdsk8r/template-project-selenium4

## Installation

The library is published to Maven Central as `no.sanchezrolfsen.framework:selenium`.

```groovy
dependencies {
    implementation 'no.sanchezrolfsen.framework:selenium:1.2.6'
}
```

The framework is implemented in Kotlin, but the public API only uses plain Java types
(interfaces, enums, `WebElement`/`WebDriver` from Selenium, etc.), so it can be consumed
seamlessly from either a Java or a Kotlin test project.

## Package overview

| Package                                         | Contents                                                                                                                   |
|-------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| `no.sanchezrolfsen.framework.selenium`          | Core pieces: `Browser`, `AbstractHooks`, `SeleniumUtils`, `TestUtils`, `CookieStore`, `SeleniumCookieFilter`               |
| `no.sanchezrolfsen.framework.selenium.config`   | `BrowserConfig`/`BrowserConfigImpl` (how the browser should be started) and `BrowserType`                                  |
| `no.sanchezrolfsen.framework.selenium.elements` | Small wrappers (`Checkbox`, `Label`, `Radiobutton`, `Table`) for common HTML widgets                                       |
| `no.sanchezrolfsen.framework.selenium.rest`     | `Config`/`CookieRetriever` interfaces used to share a browser session's cookies with REST calls made from step definitions |

### `Browser`

A singleton (Kotlin `object`) that owns the current `WebDriver` instance for the running test.
It supports both a local browser and a remote one via Selenium Grid.

```kotlin
val config: BrowserConfig = BrowserConfigImpl(BrowserType.CHROME_HEADLESS, false, null)
Browser.init(config)

val driver = Browser.vanillaDriver()
driver.get("https://example.com")

// Waits and interactions
Browser.pause().until(ExpectedConditions.titleContains("Example"))
```

Supported `BrowserType`s: `CHROME`, `CHROME_HEADLESS`, `FIREFOX`, `FIREFOX_HEADLESS`.
If `BrowserConfig.getSeleniumGridAddress()` is set, `Browser.init()` automatically connects
to that Selenium Grid address instead of launching a local browser, retrying a few times if
the grid isn't reachable yet.

### `BrowserConfig` / `BrowserConfigImpl`

Describes how the browser should be created. `BrowserConfigImpl` is a small, mutable
configuration holder with sensible defaults (`CHROME`, logging off, no grid address):

```kotlin
// Defaults: Chrome, local, no browser-log printing
val defaultConfig: BrowserConfig = BrowserConfigImpl()

// Customize via named constructor arguments
val remoteConfig: BrowserConfig = BrowserConfigImpl(
    browserType = BrowserType.FIREFOX,
    printBrowserLog = true,
    seleniumGridAddress = "http://localhost:4444/wd/hub"
)
```

### `AbstractHooks`

Extend this class in your project's Cucumber `@Before`/`@After` hooks to get a browser
started once per test run and a screenshot embedded in the report whenever a scenario fails.

```kotlin
class Hooks : AbstractHooks() {

    override fun beforeAll() {
        Browser.init(getBrowserConfig())
    }

    override fun getBrowserConfig(): BrowserConfig = BrowserConfigImpl()

    @Before
    fun before() {
        beforeEach()
    }

    @After
    fun after(scenario: Scenario) {
        afterFailedScenario(scenario)
    }
}
```

### `SeleniumUtils`

A collection of static helpers for the things you end up writing by hand in most Selenium
projects: waiting for an element to be clickable/visible with retry-friendly exception
handling, retrying clicks/`sendKeys`, safely checking visibility without throwing, taking
and saving screenshots, scrolling an element into view, printing the browser console log,
and more.

```kotlin
SeleniumUtils.waitFor(By.id("submit")).click()
SeleniumUtils.retryingSendKeys(By.id("email"), "user@example.com")
val visible = SeleniumUtils.safeIsVisible(By.id("banner"))
```

### `TestUtils`

Small string/number formatting helpers used across step definitions: parsing numbers out
of formatted text (`"114,045"` -> `114045`), building URIs, stripping whitespace/non-numeric
characters, adding thousand separators, and localized month names.

### `elements` package

Thin wrappers around common HTML widgets built on top of a `By` locator, so step
definitions can express intent instead of raw Selenium calls:

```kotlin
val termsCheckbox = Checkbox(By.id("terms"))
termsCheckbox.waitAndClick()

val resultsTable = Table(driver.findElement(By.id("results")))
val firstCell = resultsTable.row(1).getFirstColumn().getText()
```

### `rest` package

`Config` and `CookieRetriever` are small interfaces meant to be implemented by your project
so that a REST client (e.g. JAX-RS) can share the browser's session cookie via
`SeleniumCookieFilter`, and `CookieStore` is a convenience wrapper for turning a
`Set<Cookie>` into a `Cookie:`-header-friendly string.

## Building and running tests

```powershell
.\gradlew build
```

This compiles the Kotlin sources, runs the JUnit 5 unit test suite (AssertJ + Mockito), and
produces the sources/javadoc jars used for publishing.

## License

Apache License, Version 2.0 - see [LICENSE](LICENSE).
