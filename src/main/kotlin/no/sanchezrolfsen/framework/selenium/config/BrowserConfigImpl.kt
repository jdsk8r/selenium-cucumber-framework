package no.sanchezrolfsen.framework.selenium.config

import org.apache.commons.lang3.StringUtils.isNotBlank
import org.slf4j.LoggerFactory

class BrowserConfigImpl @JvmOverloads constructor(
    browserType: BrowserType = BrowserType.CHROME,
    printBrowserLog: Boolean = false,
    seleniumGridAddress: String? = null
) : BrowserConfig {

    private val log = LoggerFactory.getLogger(BrowserConfigImpl::class.java)

    private var browserType: BrowserType = browserType
    private var printBrowserLog: Boolean = printBrowserLog
    private var seleniumGridAddress: String? = null

    init {
        setSeleniumGridAddress(seleniumGridAddress)
    }

    override fun getBrowserType(): BrowserType = browserType

    fun setBrowserType(browserType: BrowserType) {
        this.browserType = browserType
    }

    override fun isPrintBrowserLog(): Boolean = printBrowserLog

    fun setPrintBrowserLog(printBrowserLog: Boolean) {
        this.printBrowserLog = printBrowserLog
    }

    override fun getSeleniumGridAddress(): String? = seleniumGridAddress

    fun setSeleniumGridAddress(address: String?) {
        if (isNotBlank(address)) {
            this.seleniumGridAddress = address
        } else {
            log.debug("The address to selenium grid is empty it will be set to null")
            this.seleniumGridAddress = null
        }
    }

    override fun isRunningRemote(): Boolean = isNotBlank(seleniumGridAddress)

    override fun printConfig() {
        log.info("==========================================================================")
        log.info("Selenium-configuration")
        log.info("--------------------------------------------------------------------------")
        log.info("Browser type:         {}", browserType.name)
        log.info("Running type:          {}", if (isRunningRemote()) "Remote" else "Local")
        log.info("Print browser log:   {}", if (printBrowserLog) "Yes" else "No")
        if (isRunningRemote()) log.info("Selenium Grid-address: {}", seleniumGridAddress)
        log.info("==========================================================================")
    }
}
