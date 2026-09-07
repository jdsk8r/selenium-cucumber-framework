package no.sanchezrolfsen.framework.selenium.elements

import no.sanchezrolfsen.framework.selenium.Browser
import no.sanchezrolfsen.framework.selenium.SeleniumUtils.safeIsVisible
import no.sanchezrolfsen.framework.selenium.SeleniumUtils.waitFor
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class Label {
    val labelSelector: By

    constructor(element: WebElement) {
        labelSelector = By.cssSelector("Label[for='${element.getAttribute("id")}']")
    }

    constructor(id: String) {
        labelSelector = By.cssSelector("Label[for='$id']")
    }

    fun click() {
        getDriver().findElement(labelSelector).click()
    }

    fun waitAndClick() {
        waitFor(labelSelector).click()
    }

    fun getText(): String = getDriver().findElement(labelSelector).text

    fun isDisplayed(): Boolean = safeIsVisible(labelSelector)

    fun getElement(): WebElement = getDriver().findElement(labelSelector)

    private fun getDriver(): WebDriver = Browser.vanillaDriver()
}
