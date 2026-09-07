package no.sanchezrolfsen.framework.selenium

import io.cucumber.java.Scenario
import no.sanchezrolfsen.framework.selenium.config.BrowserConfig
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class AbstractHooks {

    private val log = LoggerFactory.getLogger(AbstractHooks::class.java)

    fun beforeEach() {
        if (runBeforeAll) {
            beforeAll()
        }
    }

    abstract fun beforeAll()

    abstract fun getBrowserConfig(): BrowserConfig

    /**
     * method that saves screenshot for a failed scenario
     */
    fun afterFailedScenario(scenario: Scenario) {
        if (scenario.isFailed) {
            SeleniumUtils.embedScreenshot(scenario)
            val fileName = "${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd_HH-mm"))}_" +
                "${scenario.name.replace(Regex("[^\\w.-]"), "_")}.png"
            SeleniumUtils.saveScreenshot(scenario, "target" + File.separator + "screenshots", fileName)
            if (getBrowserConfig().isPrintBrowserLog()) SeleniumUtils.printBrowserLog()
        }
    }

    /**
     * method to exit running test
     */
    @Synchronized
    fun unexpectedShutdown(errorMessage: String) {
        log.warn(errorMessage)
        if (EXIT_HARD) Runtime.getRuntime().exit(0)
        exitTestRun = true
    }

    companion object {
        const val EXIT_HARD = true

        @JvmField
        var runBeforeAll = true

        @JvmField
        var exitTestRun = false
    }
}
