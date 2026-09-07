package no.sanchezrolfsen.framework.selenium

import io.cucumber.java.Scenario
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils.isBlank
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.ElementNotInteractableException
import org.openqa.selenium.JavascriptException
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.NotFoundException
import org.openqa.selenium.OutputType
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.time.Duration
import java.util.function.Consumer

object SeleniumUtils {

    private val log = LoggerFactory.getLogger(SeleniumUtils::class.java)

    const val MAX_ATTEMPTS = 4
    const val WAIT_TIME = 1

    @JvmStatic
    fun getDoubleFromInput(el: WebElement): Double? = TestUtils.doubleFromString(getInputText(el))

    @JvmStatic
    fun getIntegerFromInput(el: WebElement): Int? = TestUtils.integerFromString(getInputText(el))

    @JvmStatic
    @JvmOverloads
    fun waitForElementToBeClickable(element: WebElement, timeout: Long = Browser.globalWaitTimeout): WebElement =
        Browser.pause(timeout)
            .ignoreAll(
                listOf(
                    NoSuchElementException::class.java, JavascriptException::class.java,
                    StaleElementReferenceException::class.java
                )
            )
            .pollingEvery(Duration.ofMillis(100L))
            .until(ExpectedConditions.elementToBeClickable(element))

    @JvmStatic
    fun clearInputWithSendKeys(webElement: WebElement) {
        webElement.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"))
        webElement.sendKeys(org.openqa.selenium.Keys.BACK_SPACE)
    }

    @JvmStatic
    @JvmOverloads
    fun waitFor(element: WebElement, timeout: Long = Browser.globalWaitTimeout): WebElement =
        Browser.pause(timeout)
            .ignoreAll(
                listOf(
                    StaleElementReferenceException::class.java, ElementNotInteractableException::class.java,
                    NoSuchElementException::class.java, JavascriptException::class.java
                )
            )
            .pollingEvery(Duration.ofMillis(100L))
            .until(ExpectedConditions.visibilityOf(element))

    @JvmStatic
    @JvmOverloads
    fun waitFor(locator: By, timeout: Long = Browser.globalWaitTimeout): WebElement =
        Browser.pause(timeout)
            .ignoreAll(
                listOf(
                    StaleElementReferenceException::class.java, ElementNotInteractableException::class.java,
                    NoSuchElementException::class.java, JavascriptException::class.java
                )
            )
            .pollingEvery(Duration.ofMillis(100L))
            .until(ExpectedConditions.visibilityOfElementLocated(locator))

    @JvmStatic
    @JvmOverloads
    fun waitForElementToHaveFocus(element: WebElement, timeout: Long = Browser.globalWaitTimeout) {
        Browser.pause(timeout)
            .pollingEvery(Duration.ofMillis(100L))
            .until(ExpectedCondition { driver -> element == requireNotNull(driver).switchTo().activeElement() })
    }

    @JvmStatic
    fun wait(numberSeconds: Int) {
        try {
            Thread.sleep(numberSeconds * 1000L)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Thread.currentThread().interrupt()
        }
    }

    @JvmStatic
    fun retryingClick(element: WebElement) {
        var attempts = 0
        while (attempts < 5) {
            try {
                waitFor(element).click()
                return
            } catch (e: Exception) {
                log.info("Exception thrown, retrying click")
            }
            attempts++
        }
    }

    @JvmStatic
    fun retryingSendKeys(element: WebElement, text: String) {
        var attempts = 0
        while (attempts < 20) {
            try {
                element.sendKeys(text)
                break
            } catch (e: Exception) {
                log.info("Exception thrown, retrying sendKeys")
            }
            attempts++
        }
    }

    @JvmStatic
    fun retryingSendKeys(locator: By, text: String) {
        var attempts = 0
        while (attempts < 20) {
            try {
                Browser.vanillaDriver().findElement(locator).sendKeys(text)
                break
            } catch (e: Exception) {
                log.info("Exception thrown, retrying sendKeys")
            }
            attempts++
        }
    }

    @JvmStatic
    fun stringListContainsString(elementList: List<String>, str: String): Boolean =
        elementList.any { it.contains(str) }

    @JvmStatic
    fun elementContainsString(element: WebElement, str: String): Boolean = element.text.contains(str)

    @JvmStatic
    fun elementToStringList(elements: List<WebElement>): List<String> = elements.map { it.text }

    @JvmStatic
    fun elementListContainsString(elements: List<WebElement>, str: String): Boolean =
        stringListContainsString(elementToStringList(elements), str)

    @JvmStatic
    fun acceptAlertIfPresent(): Boolean {
        try {
            log.info("Waiting on JS-popup")
            Browser.pause(1).until(ExpectedConditions.alertIsPresent())
            closeAlert()
            return true
        } catch (e: TimeoutException) {
            log.debug(e.toString())
        }
        return false
    }

    @JvmStatic
    fun getInputText(element: WebElement): String? = element.getAttribute("value")

    @JvmStatic
    fun clearInputIfNotBlank(element: WebElement) {
        if (!isInputBlank(element)) {
            element.clear()
        }
    }

    @JvmStatic
    fun clearInputIfNotEqual(el: WebElement, text: String) {
        if (el.text != text) el.clear()
    }

    @JvmStatic
    fun isInputBlank(element: WebElement): Boolean = isBlank(getInputText(element))

    @JvmStatic
    fun closeAlert() {
        val alert: Alert = Browser.vanillaDriver().switchTo().alert()
        alert.accept()
    }

    @JvmStatic
    fun safeSelect(element: WebElement, valueToBeSelected: String) {
        Select(waitFor(element)).selectByValue(valueToBeSelected)
    }

    @JvmStatic
    fun safeIsSelected(by: By): Boolean {
        return try {
            Browser.vanillaDriver().findElement(by).isSelected
        } catch (e: ElementNotInteractableException) {
            log.debug(e.toString())
            false
        } catch (e: NoSuchElementException) {
            log.debug(e.toString())
            false
        } catch (e: StaleElementReferenceException) {
            log.debug(e.toString())
            false
        }
    }

    @JvmStatic
    fun safeIsVisible(by: By): Boolean {
        return try {
            safeIsVisible(Browser.vanillaDriver().findElement(by))
        } catch (e: ElementNotInteractableException) {
            false
        } catch (e: StaleElementReferenceException) {
            false
        } catch (e: NotFoundException) {
            false
        }
    }

    @JvmStatic
    fun safeIsVisible(element: WebElement): Boolean {
        return try {
            !(element.size.height == 0 || element.size.width == 0)
        } catch (e: ElementNotInteractableException) {
            log.debug("safeIsVisible: $e")
            false
        } catch (e: StaleElementReferenceException) {
            log.debug("safeIsVisible: $e")
            false
        } catch (e: NotFoundException) {
            log.debug("safeIsVisible: $e")
            false
        }
    }

    @JvmStatic
    fun safeIsVisibleInside(parentElement: WebElement, by: By): Boolean {
        return try {
            parentElement.findElement(by).size != Dimension(0, 0)
        } catch (e: ElementNotInteractableException) {
            log.debug("safeIsVisible: $e")
            false
        } catch (e: StaleElementReferenceException) {
            log.debug("safeIsVisible: $e")
            false
        } catch (e: NotFoundException) {
            log.debug("safeIsVisible: $e")
            false
        }
    }

    @JvmStatic
    fun embedScreenshot(scenario: Scenario) {
        val takesScreenshot = Browser.vanillaDriver() as TakesScreenshot
        scenario.attach(takesScreenshot.getScreenshotAs(OutputType.BYTES), "image/png", "Screenshot")
        log.info("Screenshot of error included in the test-report")
    }

    @JvmStatic
    fun saveScreenshot(scenario: Scenario, filepath: String, filename: String) {
        try {
            FileUtils.copyFile(
                (Browser.vanillaDriver() as TakesScreenshot).getScreenshotAs(OutputType.FILE),
                File(filepath, filename)
            )
            log.info("Screenshot of error from scenario {} saved to {}/{}", scenario.name, filepath, filename)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun printBrowserLog() {
        val logs = Browser.vanillaDriver().manage().logs().get("browser")
        val entries = logs.all
        for (entry in entries) {
            log.info(entry.toString())
        }
    }

    @JvmStatic
    fun scrollTo(webElement: WebElement) {
        val javascriptExecutor = Browser.vanillaDriver() as JavascriptExecutor
        javascriptExecutor.executeScript("arguments[0].scrollIntoView();", webElement)
    }

    @JvmStatic
    @JvmOverloads
    @Throws(InterruptedException::class)
    fun safeExecute(we: WebElement, webElementConsumer: Consumer<WebElement>, wait: Int = WAIT_TIME, attempts: Int = MAX_ATTEMPTS) {
        val executed = waitLoop(we, webElementConsumer, wait, attempts)
        if (!executed) {
            throw InterruptedException("safeExecute failed")
        }
    }

    private fun waitLoop(we: WebElement, webElementConsumer: Consumer<WebElement>, waitTime: Int, attempts: Int): Boolean {
        var attempt = 0
        var executed = false
        while (attempt < attempts && !executed) {
            Browser.pause().until(ExpectedCondition { safeIsVisible(we) })
            try {
                webElementConsumer.accept(we)
                executed = true
            } catch (e: ElementNotInteractableException) {
                attempt += 1
                wait(waitTime)
            } catch (e: StaleElementReferenceException) {
                attempt += 1
                wait(waitTime)
            } catch (e: NotFoundException) {
                attempt += 1
                wait(waitTime)
            }
        }
        return executed
    }
}
