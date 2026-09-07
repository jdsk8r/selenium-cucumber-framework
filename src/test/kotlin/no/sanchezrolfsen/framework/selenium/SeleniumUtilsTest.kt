package no.sanchezrolfsen.framework.selenium

import no.sanchezrolfsen.framework.selenium.config.MockitoExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebElement
import org.assertj.core.api.Assertions.assertThat

@ExtendWith(MockitoExtension::class)
class SeleniumUtilsTest {

    @Mock
    lateinit var element1: WebElement

    @Mock
    lateinit var element2: WebElement

    @Test
    fun getDoubleFromInput() {
        `when`(element1.getAttribute("value")).thenReturn("999.99")
        assertThat(SeleniumUtils.getDoubleFromInput(element1)).isEqualTo(999.99)
    }

    @Test
    fun getIntegerFromInput() {
        `when`(element1.getAttribute("value")).thenReturn("1234")
        assertThat(SeleniumUtils.getIntegerFromInput(element1)).isEqualTo(1234)
    }

    @Test
    fun stringListContainsString() {
        assertThat(SeleniumUtils.stringListContainsString(listOf("abc", "bcd"), "cd")).isTrue()
        assertThat(SeleniumUtils.stringListContainsString(listOf("abc", "bcd"), "de")).isFalse()
    }

    @Test
    fun elementContainsString() {
        `when`(element1.text).thenReturn("a")
        assertThat(SeleniumUtils.elementContainsString(element1, "a")).isTrue()
    }

    @Test
    fun elementToStringList() {
        `when`(element1.text).thenReturn("abc")
        `when`(element2.text).thenReturn("bcd")
        val result = SeleniumUtils.elementToStringList(listOf(element1, element2))
        assertThat(result[0]).isEqualTo("abc")
        assertThat(result[1]).isEqualTo("bcd")
    }

    @Test
    fun elementListContainsString() {
        `when`(element1.text).thenReturn("abc")
        `when`(element2.text).thenReturn("bcd")
        val elementList = listOf(element1, element2)
        assertThat(SeleniumUtils.elementListContainsString(elementList, "abc")).isTrue()
        assertThat(SeleniumUtils.elementListContainsString(elementList, "trf")).isFalse()
    }

    @Test
    fun clearOnlyIfNotBlank() {
        `when`(element1.getAttribute("value")).thenReturn("abc")
        SeleniumUtils.clearInputIfNotBlank(element1)
        Mockito.verify(element1, times(1)).clear()
        `when`(element2.getAttribute("value")).thenReturn(" ")
        SeleniumUtils.clearInputIfNotBlank(element2)
        Mockito.verify(element2, times(0)).clear()
    }

    @Test
    fun clearInputIfNotEqual() {
        `when`(element1.text).thenReturn("abc")
        SeleniumUtils.clearInputIfNotEqual(element1, "def")
        Mockito.verify(element1, times(1)).clear()
        `when`(element2.text).thenReturn("def")
        SeleniumUtils.clearInputIfNotEqual(element2, "def")
        Mockito.verify(element2, times(0)).clear()
    }

    @Test
    fun getInputText() {
        val expected = "getInputText"
        `when`(element1.getAttribute("value")).thenReturn(expected)
        assertThat(SeleniumUtils.getInputText(element1)).isEqualTo(expected)
    }

    @Test
    fun isInputBlank() {
        `when`(element1.getAttribute("value")).thenReturn("isInputBlank")
        assertThat(SeleniumUtils.isInputBlank(element1)).isFalse()
        `when`(element1.getAttribute("value")).thenReturn(" ")
        assertThat(SeleniumUtils.isInputBlank(element1)).isTrue()
    }

    @Test
    fun safeIsVisible() {
        `when`(element1.size).thenReturn(Dimension(1, 1))
        assertThat(SeleniumUtils.safeIsVisible(element1)).isTrue()
        `when`(element1.size).thenReturn(Dimension(0, 1))
        assertThat(SeleniumUtils.safeIsVisible(element1)).isFalse()
        `when`(element1.size).thenReturn(Dimension(1, 0))
        assertThat(SeleniumUtils.safeIsVisible(element1)).isFalse()
        `when`(element1.size).thenReturn(Dimension(1, 0))
        assertThat(SeleniumUtils.safeIsVisible(element1)).isFalse()
    }
}
