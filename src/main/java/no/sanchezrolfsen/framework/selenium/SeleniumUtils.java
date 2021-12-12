package no.sanchezrolfsen.framework.selenium;

import com.google.common.collect.ImmutableList;
import io.cucumber.java.Scenario;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static no.sanchezrolfsen.framework.selenium.Browser.Wait;
import static no.sanchezrolfsen.framework.selenium.TestUtils.doubleFromString;
import static no.sanchezrolfsen.framework.selenium.TestUtils.integerFromString;


@Slf4j
@UtilityClass
public class SeleniumUtils {

    public static final int maxAttempts = 4;
    public static final int waitTime = 1;

    public static Double getDoubleFromInput(WebElement el) {
        return doubleFromString(getInputText(el));
    }

    public static Integer getIntegerFromInput(WebElement el) {
        return integerFromString(getInputText(el));
    }

    public static WebElement waitForElementToBeClickable(WebElement element) {
        return waitForElementToBeClickable(element, Browser.globalWaitTimeout);
    }

    public static WebElement waitForElementToBeClickable(WebElement element, long timeout) {
        return Browser.Wait(timeout)
                .ignoreAll(ImmutableList.of(
                        NoSuchElementException.class, JavascriptException.class,
                        StaleElementReferenceException.class))
                .pollingEvery(Duration.ofMillis(100L))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void clearInputWithSendKeys(WebElement webElement) {
        webElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        webElement.sendKeys(Keys.BACK_SPACE);
    }

    public static WebElement waitFor(WebElement element) {
        return waitFor(element, Browser.globalWaitTimeout);
    }

    public static WebElement waitFor(WebElement element, long timeout) {
        return Browser.Wait(timeout)
                .ignoreAll(ImmutableList.of(StaleElementReferenceException.class, ElementNotVisibleException.class,
                        NoSuchElementException.class, JavascriptException.class))
                .pollingEvery(Duration.ofMillis(100L))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitFor(By locator) {
        return waitFor(locator, Browser.globalWaitTimeout);
    }

    public static WebElement waitFor(By locator, long timeout) {
        return Browser.Wait(timeout)
                .ignoreAll(ImmutableList.of(StaleElementReferenceException.class, ElementNotVisibleException.class,
                        NoSuchElementException.class, JavascriptException.class))
                .pollingEvery(Duration.ofMillis(100L))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForElementToHaveFocus(WebElement element) {
        waitForElementToHaveFocus(element, Browser.globalWaitTimeout);
    }

    public static void waitForElementToHaveFocus(final WebElement element, long timeout) {
        Browser.Wait(timeout)
                .pollingEvery(Duration.ofMillis(100L))
                .until((ExpectedCondition<Boolean>) driver -> element.equals(requireNonNull(driver).switchTo().activeElement()));
    }

    public static void wait(int numberSeconds) {
        try {
            Thread.sleep(numberSeconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void retryingClick(WebElement element) {
        int attempts = 0;
        while (attempts < 5) {
            try {
                waitFor(element).click();
                return;
            } catch (Exception e) {
                log.info("Exception thrown, retrying click");
            }
            attempts++;
        }
    }

    public static void retryingSendKeys(WebElement element, String text) {
        int attempts = 0;
        while (attempts < 20) {
            try {
                element.sendKeys(text);
                break;
            } catch (Exception e) {
                log.info("Exception thrown, retrying sendKeys");
            }
            attempts++;
        }
    }

    public static void retryingSendKeys(By locator, String text) {
        int attempts = 0;
        while (attempts < 20) {
            try {
                Browser.vanillaDriver().findElement(locator).sendKeys(text);
                break;
            } catch (Exception e) {
                log.info("Exception thrown, retrying sendKeys");
            }
            attempts++;
        }
    }

    public static boolean stringListContainsString(List<String> elementList, String str) {
        return elementList.stream().anyMatch(s -> s.contains(str));
    }

    public static boolean elementContainsString(WebElement element, String str) {
        return element.getText().contains(str);
    }

    public static List<String> elementToStringList(List<WebElement> elements) {
        return elements.stream().map(WebElement::getText).collect(toList());
    }

    public static boolean elementListContainsString(List<WebElement> elements, String str) {
        return stringListContainsString(elementToStringList(elements), str);
    }

    public static boolean acceptAlertIfPresent() {
        try {
            log.info("Waiting on JS-popup");
            Browser.Wait(1).until(ExpectedConditions.alertIsPresent());
            closeAlert();
            return true;
        } catch (TimeoutException e) {
            log.debug(e.toString());
        }
        return false;
    }

    public static String getInputText(WebElement element) {
        return element.getAttribute("value");
    }

    public static void clearInputIfNotBlank(WebElement element) {
        if (!isInputBlank(element)) {
            element.clear();
        }
    }

    public static void clearInputIfNotEqual(WebElement el, String text) {
        if (!el.getText().equals(text))
            el.clear();
    }

    public static boolean isInputBlank(WebElement element) {
        return isBlank(getInputText(element));
    }

    public static void closeAlert() {
        Alert alert = Browser.vanillaDriver().switchTo().alert();
        alert.accept();
    }

    public static void safeSelect(final WebElement element, String valueToBeSelected) {
        new Select(waitFor(element)).selectByValue(valueToBeSelected);
    }

    public static boolean safeIsSelected(By by) {
        try {
            return Browser.vanillaDriver().findElement(by).isSelected();
        } catch (ElementNotVisibleException | NoSuchElementException | StaleElementReferenceException e) {
            log.debug(e.toString());
            return false;
        }
    }

    public static boolean safeIsVisible(By by) {
        try {
            return safeIsVisible(Browser.vanillaDriver().findElement(by));
        } catch (ElementNotVisibleException | StaleElementReferenceException | NotFoundException e) {
            return false;
        }
    }

    public static boolean safeIsVisible(WebElement element) {
        try {
            return !(element.getSize().getHeight() == 0 || element.getSize().getWidth() == 0);
        } catch (ElementNotVisibleException | StaleElementReferenceException | NotFoundException e) {
            log.debug("safeIsVisible: " + e);
            return false;
        }
    }

    public static boolean safeIsVisibleInside(WebElement parentElement, By by) {
        try {
            return !parentElement.findElement(by).getSize().equals(new Dimension(0, 0));
        } catch (ElementNotVisibleException | StaleElementReferenceException | NotFoundException e) {
            log.debug("safeIsVisible: " + e);
            return false;
        }
    }

    public static void embedScreenshot(Scenario scenario) {
        TakesScreenshot takesScreenshot = (TakesScreenshot) Browser.vanillaDriver();
        scenario.attach(takesScreenshot.getScreenshotAs(OutputType.BYTES), "image/png", "Screenshot");
        log.info("Screenshot of error included in the test-report");
    }

    public static void saveScreenshot(Scenario scenario, String filepath, String filename) {
        try {
            FileUtils.copyFile(((TakesScreenshot) Browser.vanillaDriver()).getScreenshotAs(OutputType.FILE),
                    new File(filepath, filename));
            log.info("Screenshot of error saved to {}/{}", filepath, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printBrowserLog() {
        LogEntries logs = Browser.vanillaDriver().manage().logs().get("browser");
        List<LogEntry> entries = logs.getAll();
        for (LogEntry entry : entries) {
            log.info(entry.toString());
        }
    }

    public static void scrollTo(WebElement webElement) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) Browser.vanillaDriver();
        javascriptExecutor.executeScript("arguments[0].scrollIntoView();", webElement);
    }

    public static void safeExecute(WebElement we, Consumer<WebElement> webElementConsumer) {
        boolean executed = waitLoop(we, webElementConsumer, waitTime, maxAttempts);
        if (!executed) {
            throw new RuntimeException("safeExecute failed");
        }
    }

    private static boolean waitLoop(WebElement we, Consumer<WebElement> webElementConsumer, int waitTime, int attempts) {
        int attempt = 0;
        boolean executed = false;
        while (attempt < attempts && !executed) {
            Browser.Wait().until((ExpectedCondition<Boolean>) el -> SeleniumUtils.safeIsVisible(we));
            try {
                webElementConsumer.accept(we);
                executed = true;
            } catch (ElementNotVisibleException | StaleElementReferenceException | NotFoundException e) {
                attempt += 1;
                SeleniumUtils.wait(waitTime);
            }
        }
        return executed;
    }

    public static void safeExecute(WebElement we, Consumer<WebElement> webElementConsumer, int wait, int attempts) {
        boolean executed = waitLoop(we, webElementConsumer, wait, attempts);
        if (!executed) {
            throw new RuntimeException("safeExecute failed");
        }
    }
}
