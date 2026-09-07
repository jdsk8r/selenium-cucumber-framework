package no.sanchezrolfsen.framework.selenium

import org.apache.commons.lang3.math.NumberUtils
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

object TestUtils {

    @JvmStatic
    fun addThousandSeparator(n: String): String =
        n.replace(Regex("(?<=\\d)(?=(\\d\\d\\d)+(?!\\d))"), " ")

    @JvmStatic
    fun doubleFromString(string: String?): Double? {
        val numberString = verifyAndFormat(string, false) ?: return null
        return numberString.toDouble()
    }

    @JvmStatic
    fun longFromString(string: String?): Long? {
        val numberString = verifyAndFormat(string, false) ?: return null
        return numberString.toLong()
    }

    @JvmStatic
    fun integerFromString(string: String?): Int? {
        val numberString = verifyAndFormat(string, false) ?: return null
        return numberString.toInt()
    }

    @JvmStatic
    fun extractIntegerFromString(string: String?): Int? {
        val numberString = verifyAndFormat(string, true) ?: return null
        return numberString.toInt()
    }

    private fun verifyAndFormat(string: String?, ignoreNonNumeric: Boolean): String? {
        if (string == null) return null
        val formatted = if (ignoreNonNumeric) removeNonNumeric(string) else removeWhitespace(string)
        return if (NumberUtils.isParsable(formatted)) formatted else null
    }

    @JvmStatic
    @JvmOverloads
    fun formatAsURI(host: String, port: Int, path: String, useHTTPS: Boolean = false): String {
        val protocol = if (useHTTPS) "https" else "http"
        val portString = if ((useHTTPS && port == 443) || (!useHTTPS && port == 80)) "" else ":$port"
        return "$protocol://$host$portString/$path"
    }

    @JvmStatic
    fun removeWhitespace(string: String): String = string.replace(Regex("\\s+"), "")

    @JvmStatic
    fun removeNonNumeric(string: String): String = string.replace(Regex("\\D+"), "")

    @JvmStatic
    fun intToMonth(month: Int, locale: Locale): String =
        Month.of(month).getDisplayName(TextStyle.FULL, locale)
}
