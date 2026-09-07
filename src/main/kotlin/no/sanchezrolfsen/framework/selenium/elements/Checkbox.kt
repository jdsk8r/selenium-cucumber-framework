package no.sanchezrolfsen.framework.selenium.elements

import no.sanchezrolfsen.framework.selenium.Browser
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class Checkbox(private val selector: By) {

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

    fun getElement(): WebElement = getDriver().findElement(selector)

    fun getLabel(): Label = Label(getElement())

    private fun getDriver(): WebDriver = Browser.vanillaDriver()
}
