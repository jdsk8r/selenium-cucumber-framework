package no.sanchezrolfsen.framework.selenium

import no.sanchezrolfsen.framework.selenium.config.BrowserConfig
import no.sanchezrolfsen.framework.selenium.config.BrowserType
import org.openqa.selenium.InvalidArgumentException
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.LocalFileDetector
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.remote.UnreachableBrowserException
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.security.InvalidParameterException
import java.time.Duration
import java.util.concurrent.TimeUnit

object Browser {
    private val log = LoggerFactory.getLogger(Browser::class.java)

    const val DEFAULT_IMPLICIT_WAIT = 0
    const val DEFAULT_SCRIPT_TIMEOUT = 8
    const val DEFAULT_WAIT_TIMEOUT = 15

    @JvmField
    var globalWaitTimeout: Long = 0

    private var useRemoteDriver = false
    private var driver: WebDriver? = null
    private var jsExecutor: JavascriptExecutor? = null
    private var standardWait: WebDriverWait? = null

    /**
     * Method to initialize the browser driver
     */
    @Throws(MalformedURLException::class)
    @JvmStatic
    fun init(browserConfig: BrowserConfig) {
        val seleniumGridUrl = browserConfig.getSeleniumGridAddress()
        if (!seleniumGridUrl.isNullOrBlank()) {
            for (i in 0..5) {
                try {
                    createExternalBrowser(browserConfig.getBrowserType(), URI.create(seleniumGridUrl).toURL())
                    break
                } catch (unreachableBrowserException: UnreachableBrowserException) {
                    log.debug("Didn't manage to start browser against $seleniumGridUrl on try nr $i. Waiting 10 s")
                    try {
                        TimeUnit.SECONDS.sleep(10)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        Thread.currentThread().interrupt()
                    }
                }
            }
        } else {
            createLocalBrowser(browserConfig.getBrowserType())
        }

        setBrowserTimeouts(DEFAULT_SCRIPT_TIMEOUT, DEFAULT_IMPLICIT_WAIT, DEFAULT_WAIT_TIMEOUT)
    }

    /**
     * Method to create a local browser
     */
    private fun createLocalBrowser(browser: BrowserType) {
        @Suppress("REDUNDANT_ELSE_IN_WHEN")
        when (browser) {
            BrowserType.CHROME -> {
                val chromeOptions = ChromeOptions()
                driver = ChromeDriver(chromeOptions)
                log.debug("Chrome selected as the desired browser.")
            }
            BrowserType.CHROME_HEADLESS -> {
                val chromeOptions2 = ChromeOptions()
                chromeOptions2.addArguments("--headless=new")
                driver = ChromeDriver(chromeOptions2)
                log.debug("Chrome Headless selected as the desired browser.")
            }
            BrowserType.FIREFOX -> {
                driver = FirefoxDriver()
                log.debug("Firefox selected as the desired browser.")
            }
            BrowserType.FIREFOX_HEADLESS -> {
                val firefoxOptions = FirefoxOptions()
                firefoxOptions.addArguments("-headless")
                driver = FirefoxDriver(firefoxOptions)
                log.debug("Firefox Headless selected as the desired browser.")
            }
            else -> throw InvalidArgumentException("Browser '${browser.name}' is not implemented on the framework")
        }
    }

    /**
     * Method to create external browser (grid)
     */
    private fun createExternalBrowser(browser: BrowserType, seleniumGridUrl: URL) {
        useRemoteDriver = true
        val remoteWebDriver: RemoteWebDriver
        when (browser) {
            BrowserType.CHROME -> {
                val chromeOptions = ChromeOptions()
                remoteWebDriver = RemoteWebDriver(seleniumGridUrl, chromeOptions)
                log.debug("Remote (Chrome) selected as the desired browser.")
            }
            BrowserType.FIREFOX -> {
                remoteWebDriver = RemoteWebDriver(seleniumGridUrl, FirefoxOptions())
                log.debug("Remote (Firefox) selected as the desired browser.")
            }
            else -> throw InvalidParameterException("Browser ${browser.name} for selenium grid is not implemented on the framework")
        }
        remoteWebDriver.fileDetector = LocalFileDetector() // For uploading of files from local-path
        driver = remoteWebDriver
    }

    /**
     * Method to set up browser timeouts
     */
    @JvmStatic
    fun setBrowserTimeouts(scriptTimeout: Int, implicitWait: Int, waitTimeout: Int) {
        val currentDriver = driver ?: throw NullPointerException("Browser.init() must be run before one can call the driver-instance")
        currentDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout.toLong()))
        currentDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait.toLong()))
        globalWaitTimeout = waitTimeout.toLong()
        standardWait = WebDriverWait(currentDriver, Duration.ofSeconds(waitTimeout.toLong()))
    }

    /**
     * Method that returns WebDriver, to call this method Browser.init() must be run first
     */
    @JvmStatic
    fun vanillaDriver(): WebDriver =
        driver ?: throw NullPointerException("Browser.init() must be run before one can call the driver-instance")

    /**
     * Method to run javascript on browser when running tests
     */
    @JvmStatic
    fun jsExecutor(): JavascriptExecutor {
        if (jsExecutor == null) {
            jsExecutor = vanillaDriver() as JavascriptExecutor
        }
        return jsExecutor!!
    }

    /**
     * Pause method, it waits by default 'waitTimeout' setup on method setBrowserTimeouts()
     */
    @JvmStatic
    fun pause(): WebDriverWait =
        standardWait ?: throw NullPointerException("StandardWait is not set")

    /**
     * Pause method a number of seconds
     */
    @JvmStatic
    fun pause(timeOutInSeconds: Long): WebDriverWait =
        WebDriverWait(vanillaDriver(), Duration.ofSeconds(timeOutInSeconds))

    /**
     * Method to check if the test are running remotely
     */
    @JvmStatic
    fun isRemoteDriver(): Boolean = useRemoteDriver
}
