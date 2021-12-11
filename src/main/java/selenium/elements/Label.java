package selenium.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.Browser;

import static java.lang.String.format;
import static selenium.SeleniumUtils.safeIsVisible;
import static selenium.SeleniumUtils.waitFor;

public class Label {
    final private By labelSelector;

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
