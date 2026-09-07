package no.sanchezrolfsen.framework.selenium.config

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows

class SeleniumOptionsTest {

    @Test
    fun stringToBrowserType() {
        assertThat(SeleniumOptions.stringToBrowserType("chrome")).isEqualTo(BrowserType.CHROME)
        assertThat(SeleniumOptions.stringToBrowserType("chrome_headless")).isEqualTo(BrowserType.CHROME_HEADLESS)
        assertThat(SeleniumOptions.stringToBrowserType("firefox")).isEqualTo(BrowserType.FIREFOX)
        assertThat(SeleniumOptions.stringToBrowserType("firefox_headless")).isEqualTo(BrowserType.FIREFOX_HEADLESS)
        assertThrows(IllegalArgumentException::class.java) { SeleniumOptions.stringToBrowserType("edge") }
    }
}
