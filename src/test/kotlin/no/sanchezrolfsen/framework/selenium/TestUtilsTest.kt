package no.sanchezrolfsen.framework.selenium

import org.junit.jupiter.api.Test
import java.util.Locale
import org.assertj.core.api.Assertions.assertThat

class TestUtilsTest {

    @Test
    fun addThousandSeparator() {
        assertThat(TestUtils.addThousandSeparator("1234567")).isEqualTo("1 234 567")
    }

    @Test
    fun doubleFromString() {
        assertThat(TestUtils.doubleFromString("100.99")).isEqualTo(100.99)
    }

    @Test
    fun longFromString() {
        assertThat(TestUtils.longFromString("999 999 999 999")).isEqualTo(999999999999L)
    }

    @Test
    fun integerFromString() {
        assertThat(TestUtils.integerFromString("199")).isEqualTo(199)
    }

    @Test
    fun extractIntegerFromString() {
        assertThat(TestUtils.extractIntegerFromString("114,045")).isEqualTo(114045)
        assertThat(TestUtils.extractIntegerFromString("\$123")).isEqualTo(123)
        assertThat(TestUtils.extractIntegerFromString("99 Kr")).isEqualTo(99)
    }

    @Test
    fun formatAsURI() {
        assertThat(TestUtils.formatAsURI("hostname", 8080, "test1", true)).isEqualTo("https://hostname:8080/test1")
        assertThat(TestUtils.formatAsURI("hostname", 8080, "test2", false)).isEqualTo("http://hostname:8080/test2")
    }

    @Test
    fun testFormatAsURI() {
        assertThat(TestUtils.formatAsURI("localhost", 3032, "test2")).isEqualTo("http://localhost:3032/test2")
    }

    @Test
    fun removeWhitespace() {
        assertThat(TestUtils.removeWhitespace("Lorem Ipsum is simply dummy text")).isEqualTo("LoremIpsumissimplydummytext")
    }

    @Test
    fun removeNonNumeric() {
        assertThat(TestUtils.removeNonNumeric("1adasd334412asd1")).isEqualTo("13344121")
    }

    @Test
    fun intToMonth() {
        assertThat(TestUtils.intToMonth(6, Locale.US)).isEqualTo("June")
        assertThat(TestUtils.intToMonth(10, Locale.US)).isEqualTo("October")
        assertThat(TestUtils.intToMonth(6, Locale.of("nb"))).isEqualTo("juni")
        assertThat(TestUtils.intToMonth(10, Locale.of("nb"))).isEqualTo("oktober")
    }
}
