package no.sanchezrolfsen.framework.selenium.config

internal object SeleniumOptions {

    @JvmStatic
    fun stringToBrowserType(browserType: String): BrowserType =
        when (browserType.uppercase()) {
            "CHROME" -> BrowserType.CHROME
            "CHROME_HEADLESS" -> BrowserType.CHROME_HEADLESS
            "FIREFOX" -> BrowserType.FIREFOX
            "FIREFOX_HEADLESS" -> BrowserType.FIREFOX_HEADLESS
            else -> throw IllegalArgumentException("Browser '$browserType' is not supported in the framework")
        }
}
