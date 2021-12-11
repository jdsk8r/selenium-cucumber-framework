package selenium.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.Browser;

import java.security.InvalidParameterException;

public class Radiobutton {
    private By trueElementLocator;
    private By falseElementLocator;
    private By currentLocator;

    public Radiobutton(By elementLocator) {
        currentLocator = elementLocator;
    }

    public Radiobutton(By trueElementLocator, By falseElementLocator) {
        this.trueElementLocator = trueElementLocator;
        this.falseElementLocator = falseElementLocator;
    }

    public void click() {
        getLabel().click();
    }

    public void waitAndClick() {
        getLabel().waitAndClick();
    }

    public String getText() {
        return getLabel().getText();
    }

    public boolean isDisplayed() {
        return getLabel().isDisplayed();
    }

    public boolean isChecked() {
        return getElement().isSelected();
    }

    public boolean isUnchecked() {
        return !isChecked();
    }

    public WebElement getElement() {
        if (currentLocator == null && trueElementLocator != null) {
            throw new InvalidParameterException("Radiobutton cannot be called directly if constructed with true/false locators, call yes() or no() first");
        }
        return this.getDriver().findElement(currentLocator);
    }

    public Label getLabel() {
        return new Label(getElement());
    }

    private WebDriver getDriver() {
        return Browser.vanillaDriver();
    }

    public Radiobutton yes() {
        if (trueElementLocator == null) {
            throw new InvalidParameterException("Radiobutton must be constructed with true and false locators to use yes()-calls");
        }
        currentLocator = trueElementLocator;
        return this;
    }

    public Radiobutton no() {
        if (falseElementLocator == null) {
            throw new InvalidParameterException("Radiobutton must be constructed with true and false locators to use no()-calls");
        }
        currentLocator = falseElementLocator;
        return this;
    }
}
