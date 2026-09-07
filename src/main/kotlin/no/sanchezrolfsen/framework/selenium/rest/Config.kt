package no.sanchezrolfsen.framework.selenium.rest

import no.sanchezrolfsen.framework.selenium.config.BrowserConfig
import java.time.format.DateTimeFormatter

interface Config {
    fun getBaseUrl(): String

    fun getSeleniumGridUrl(): String

    fun getBrowserConfig(): BrowserConfig

    fun printConfig()

    fun getStandardDateFormat(): DateTimeFormatter
}
