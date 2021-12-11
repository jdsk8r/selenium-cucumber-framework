package selenium.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.Browser;

public class Checkbox {
    private final By selector;

    public Checkbox(By by) {
        this.selector = by;
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
        return this.getDriver().findElement(this.selector);
    }

    public Label getLabel() {
        return new Label(getElement());
    }

    private WebDriver getDriver() {
        return Browser.vanillaDriver();
    }
}
