package no.sanchezrolfsen.framework.selenium.elements

import no.sanchezrolfsen.framework.selenium.Browser
import org.openqa.selenium.By
import org.openqa.selenium.InvalidArgumentException
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.slf4j.LoggerFactory

open class Table(protected val tableWebElement: WebElement) {

    private val log = LoggerFactory.getLogger(Table::class.java)

    protected val rows: List<WebElement>
    protected var columns: List<WebElement> = ArrayList()

    init {
        Browser.pause().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody")))
        rows = tableWebElement.findElements(By.cssSelector("tbody > tr"))
    }

    fun getRow(rowNumber: Int): WebElement {
        if (rowNumber < 1 && rowNumber > getNumberOfRows()) {
            throw InvalidArgumentException("Row-number '$rowNumber' must be in the interval 1-${getNumberOfRows()}")
        }
        return rows[rowNumber - 1] // converting rowNumber to arrayIndex
    }

    fun getNumberOfRows(): Int = rows.size

    fun firstRow(): WebElement = getRow(1)

    fun lastRow(): WebElement = getRow(getNumberOfRows())

    fun row(rowNumber: Int): Table {
        columns = getRow(rowNumber).findElements(By.tagName("td"))
        return this
    }

    fun getColumn(columnNumber: Int): WebElement {
        if (columns.isEmpty())
            log.error("You must select first a row before you can select a column, f.ex: Table().row(2).getColumn(3)")
        return columns[columnNumber - 1] // converting  columnNumber to arrayIndex
    }

    fun getNumberOfColumns(): Int = columns.size

    fun getFirstColumn(): WebElement = getColumn(1)

    fun getLastColumn(): WebElement = getColumn(getNumberOfColumns())
}
