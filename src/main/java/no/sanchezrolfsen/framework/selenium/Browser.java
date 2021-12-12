package no.sanchezrolfsen.framework.selenium;

import lombok.extern.slf4j.Slf4j;
import no.sanchezrolfsen.framework.selenium.config.BrowserConfig;
import no.sanchezrolfsen.framework.selenium.config.BrowserType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Slf4j
public class Browser {
    public static final int DEFAULT_IMPLICIT_WAIT = 0;
    public static final int DEFAULT_SCRIPT_TIMEOUT = 8;
    public static final int DEFAULT_WAIT_TIMEOUT = 15;
    public static long globalWaitTimeout;
    private static boolean useRemoteDriver = false;
    private static WebDriver driver;
    private static JavascriptExecutor jsExecutor;
    private static WebDriverWait standardWait;

    private Browser() {

    }

    public static void init(BrowserConfig browserConfig) throws MalformedURLException {
        String seleniumGridUrl = browserConfig.getSeleniumGridAddress();
        if (StringUtils.isNotBlank(seleniumGridUrl)) {
            for (int i = 0; i <= 5; i++) {
                try {
                    createExternalBrowser(browserConfig.getBrowserType(), new URL(seleniumGridUrl));
                    break;
                }
                catch (org.openqa.selenium.remote.UnreachableBrowserException unreachableBrowserException) {
                    log.debug(String.format("Didn't manage to start browser against %s on try nr %s. Waiting 10 s",seleniumGridUrl,i));
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            createLocalBrowser(browserConfig.getBrowserType());
        }

        setBrowserTimeouts(DEFAULT_SCRIPT_TIMEOUT, DEFAULT_IMPLICIT_WAIT, DEFAULT_WAIT_TIMEOUT);
    }

    private static void createLocalBrowser(BrowserType browser) {
        switch (browser) {
            case CHROME -> {
                final ChromeOptions chromeOptions = new ChromeOptions();
                driver = new ChromeDriver(chromeOptions);
                log.debug("Chrome selected as the desired browser.");
            }
            case CHROME_HEADLESS -> {
                final ChromeOptions chromeOptions2 = new ChromeOptions();
                chromeOptions2.setHeadless(true);
                driver = new ChromeDriver(chromeOptions2);
                log.debug("Chrome Headless selected as the desired browser.");
            }
            case FIREFOX -> {
                driver = new FirefoxDriver();
                log.debug("Firefox selected as the desired browser.");
            }
            case FIREFOX_HEADLESS -> {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(true);
                driver = new FirefoxDriver(firefoxOptions);
                log.debug("Firefox Headless selected as the desired browser.");
            }
            default -> throw new InvalidArgumentException(format("Browser '%s' is not implemented on the framework", browser.name()));
        }
    }

    private static void createExternalBrowser(BrowserType browser, URL seleniumGridUrl) {
        useRemoteDriver = true;
        RemoteWebDriver remoteWebDriver;
        switch (browser) {
            case CHROME -> {
                final ChromeOptions chromeOptions = new ChromeOptions();
                remoteWebDriver = new RemoteWebDriver(seleniumGridUrl, chromeOptions);
                log.debug("Remote (Chrome) selected as the desired browser.");
            }
            case FIREFOX -> {
                remoteWebDriver = new RemoteWebDriver(seleniumGridUrl, new FirefoxOptions());
                log.debug("Remote (Firefox) selected as the desired browser.");
            }
            default -> throw new InvalidParameterException(format("Browser %s for selenium grid is not implemented on the framework", browser.name()));
        }
        remoteWebDriver.setFileDetector(new LocalFileDetector()); // For uploading of files from local-path
        driver = remoteWebDriver;
    }

    public static void setBrowserTimeouts(int scriptTimeout, int implicitWait, int waitTimeout) {
        if (driver == null) {
            throw new NullPointerException("Browser.init() must be run before one can call the driver-instance");
        }
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        globalWaitTimeout = waitTimeout;
        standardWait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeout));
    }

    public static WebDriver vanillaDriver() {
        if (driver == null) {
            throw new NullPointerException("Browser.init() must be run before one can call the driver-instance");
        }
        return driver;
    }

    public static JavascriptExecutor jsExecutor() {
        if (jsExecutor == null) {
            WebDriver jsDriver = vanillaDriver();
            jsExecutor = (JavascriptExecutor) jsDriver;
        }
        return jsExecutor;
    }

    public static WebDriverWait Wait() {
        if (standardWait == null) {
            throw new NullPointerException("StandardWait is not set");
        }
        return standardWait;
    }

    public static WebDriverWait Wait(long timeOutInSeconds) {
        return new WebDriverWait(vanillaDriver(), Duration.ofSeconds(timeOutInSeconds));
    }

    public static boolean isRemoteDriver() {
        return useRemoteDriver;
    }

}
