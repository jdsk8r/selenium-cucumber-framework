package no.sanchezrolfsen.framework.selenium.elements;

import no.sanchezrolfsen.framework.selenium.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.lang.String.format;
import static no.sanchezrolfsen.framework.selenium.SeleniumUtils.safeIsVisible;
import static no.sanchezrolfsen.framework.selenium.SeleniumUtils.waitFor;

public class Label {
    final By labelSelector;

    public Label(WebElement element) {
        labelSelector = By.cssSelector(format("Label[for='%s']", element.getAttribute("id")));
    }

    public Label(String id) {
        labelSelector = By.cssSelector(format("Label[for='%s']", id));
    }

    public void click() {
        getDriver().findElement(labelSelector).click();
    }

    public void waitAndClick() {
        waitFor(labelSelector).click();
    }

    public String getText() {
        return getDriver().findElement(labelSelector).getText();
    }

    public boolean isDisplayed() {
        return safeIsVisible(labelSelector);
    }

    public WebElement getElement() {
        return getDriver().findElement(labelSelector);
    }

    private WebDriver getDriver() {
        return Browser.vanillaDriver();
    }
}
