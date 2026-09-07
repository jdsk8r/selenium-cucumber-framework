package no.sanchezrolfsen.framework.selenium.elements

import no.sanchezrolfsen.framework.selenium.Browser
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import java.security.InvalidParameterException

class Radiobutton {
    private var trueElementLocator: By? = null
    private var falseElementLocator: By? = null
    private var currentLocator: By? = null

    constructor(elementLocator: By) {
        currentLocator = elementLocator
    }

    constructor(trueElementLocator: By, falseElementLocator: By) {
        this.trueElementLocator = trueElementLocator
        this.falseElementLocator = falseElementLocator
    }

    fun click() {
        getLabel().click()
    }

    fun waitAndClick() {
        getLabel().waitAndClick()
    }

    fun getText(): String = getLabel().getText()

    fun isDisplayed(): Boolean = getLabel().isDisplayed()

    fun isChecked(): Boolean = getElement().isSelected

    fun isUnchecked(): Boolean = !isChecked()

    fun getElement(): WebElement {
        if (currentLocator == null && trueElementLocator != null) {
            throw InvalidParameterException("Radiobutton cannot be called directly if constructed with true/false locators, call yes() or no() first")
        }
        return getDriver().findElement(currentLocator!!)
    }

    fun getLabel(): Label = Label(getElement())

    private fun getDriver(): WebDriver = Browser.vanillaDriver()

    fun yes(): Radiobutton {
        val locator = trueElementLocator
            ?: throw InvalidParameterException("Radiobutton must be constructed with true and false locators to use yes()-calls")
        currentLocator = locator
        return this
    }

    fun no(): Radiobutton {
        val locator = falseElementLocator
            ?: throw InvalidParameterException("Radiobutton must be constructed with true and false locators to use no()-calls")
        currentLocator = locator
        return this
    }
}
