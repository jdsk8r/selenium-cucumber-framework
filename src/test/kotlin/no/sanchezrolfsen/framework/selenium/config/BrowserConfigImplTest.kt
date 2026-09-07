package no.sanchezrolfsen.framework.selenium.config

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

@DisplayName("Test of browser configuration")
class BrowserConfigImplTest {

    @Test
    @DisplayName("Test that the setup is correct for the default constructor")
    fun standardParameterEmptyConstructor() {
        val browserConfig: BrowserConfig = BrowserConfigImpl()
        assertThat(browserConfig.isRunningRemote()).isFalse()
        assertThat(browserConfig.getSeleniumGridAddress()).isNull()
        assertThat(browserConfig.getBrowserType()).isEqualTo(BrowserType.CHROME)
        assertThat(browserConfig.isPrintBrowserLog()).isFalse()
    }

    @Test
    @DisplayName("Test that the setup is correct for empty constructor arguments")
    fun standardParameterEmptyArgs() {
        val browserConfig: BrowserConfig = BrowserConfigImpl(seleniumGridAddress = null)
        assertThat(browserConfig.isRunningRemote()).isFalse()
        assertThat(browserConfig.getSeleniumGridAddress()).isNull()
        assertThat(browserConfig.getBrowserType()).isEqualTo(BrowserType.CHROME)
        assertThat(browserConfig.isPrintBrowserLog()).isFalse()
    }

    @Test
    @DisplayName("setSeleniumGridAddress is not set if empty")
    fun setSeleniumGridAddress() {
        val blankWhitespace = " "
        val browserConfig = BrowserConfigImpl()
        browserConfig.setSeleniumGridAddress(blankWhitespace)
        assertThat(browserConfig.getSeleniumGridAddress()).isNull()
        assertThat(browserConfig.isRunningRemote()).isFalse()
    }

    @Test
    @DisplayName("setSeleniumGridAddress sets runningRemote to true if selenium grid is not empty")
    fun setSeleniumGridAddressConstructor() {
        val blankWhitespace = " "
        val address = "address"
        var browserConfig = BrowserConfigImpl(seleniumGridAddress = address)
        assertThat(browserConfig.isRunningRemote()).isTrue()
        assertThat(browserConfig.getSeleniumGridAddress()).isEqualTo(address)

        browserConfig = BrowserConfigImpl(seleniumGridAddress = blankWhitespace)
        assertThat(browserConfig.isRunningRemote()).isFalse()
        assertThat(browserConfig.getSeleniumGridAddress()).isNull()

        browserConfig = BrowserConfigImpl(seleniumGridAddress = null)
        assertThat(browserConfig.isRunningRemote()).isFalse()
        assertThat(browserConfig.getSeleniumGridAddress()).isNull()
    }

    @Test
    @DisplayName("RemoteRunning specified afterwards it sets remote running to true")
    fun setSeleniumGridAddressOgMethod() {
        val address = "address"
        val browserConfig = BrowserConfigImpl()
        assertThat(browserConfig.isRunningRemote()).isFalse()

        browserConfig.setSeleniumGridAddress(address)
        assertThat(browserConfig.getSeleniumGridAddress()).isEqualTo(address)
        assertThat(browserConfig.isRunningRemote()).isTrue()
    }
}
