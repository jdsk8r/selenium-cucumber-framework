package no.sanchezrolfsen.framework.selenium.config

interface BrowserConfig {

    fun getBrowserType(): BrowserType

    fun isPrintBrowserLog(): Boolean

    fun isRunningRemote(): Boolean

    fun getSeleniumGridAddress(): String?

    fun printConfig()
}
