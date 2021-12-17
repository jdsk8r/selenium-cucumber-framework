package no.sanchezrolfsen.framework.selenium.elements;

import lombok.extern.slf4j.Slf4j;
import no.sanchezrolfsen.framework.selenium.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Slf4j
public class Table {
    protected final WebElement tableWebElement;
    protected final List<WebElement> rows;
    protected List<WebElement> columns = new ArrayList<>();

    public Table(WebElement tableWebElement) {
        Browser.pause().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody")));
        this.tableWebElement = tableWebElement;
        rows = tableWebElement.findElements(By.cssSelector("tbody > tr"));
    }

    public WebElement getRow(int rowNumber) {
        if (rowNumber < 1 && rowNumber > getNumberOfRows()) {
            throw new InvalidArgumentException(format("Row-number '%d' must be in the interval 1-%d", rowNumber, getNumberOfRows()));
        }
        return rows.get(rowNumber - 1); //converting rowNumber to arrayIndex
    }

    public int getNumberOfRows() {
        return rows.size();
    }

    public WebElement firstRow() {
        return getRow(1);
    }

    public WebElement lastRow() {
        return getRow(getNumberOfRows());
    }

    public Table row(int rowNumber) {
        columns = getRow(rowNumber).findElements(By.tagName("td"));
        return this;
    }

    public WebElement getColumn(int columnNumber) {
        if (columns.isEmpty())
            log.error("You must select first a row before you can select a column, f.ex: Table().row(2).getColumn(3)");
        return columns.get(columnNumber - 1); //converting  columnNumber to arrayIndex
    }

    public int getNumberOfColumns() {
        return columns.size();
    }

    public WebElement getFirstColumn() {
        return getColumn(1);
    }

    public WebElement getLastColumn() {
        return getColumn(getNumberOfColumns());
    }
}
