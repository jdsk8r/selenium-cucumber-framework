package no.sanchezrolfsen.framework.selenium;

import no.sanchezrolfsen.framework.selenium.config.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeleniumUtilsTest {

    @Mock
    WebElement element1;
    @Mock
    WebElement element2;

    @Test
    void getDoubleFromInput() {
        when(element1.getAttribute("value")).thenReturn("999.99");
        assertThat(SeleniumUtils.getDoubleFromInput(element1)).isEqualTo(999.99);
    }

    @Test
    void getIntegerFromInput() {
        when(element1.getAttribute("value")).thenReturn("1234");
        assertThat(SeleniumUtils.getIntegerFromInput(element1)).isEqualTo(1234);
    }

    @Test
    void stringListContainsString() {
        assertThat(SeleniumUtils.stringListContainsString(Arrays.asList("abc", "bcd"), "cd")).isTrue();
        assertThat(SeleniumUtils.stringListContainsString(Arrays.asList("abc", "bcd"), "de")).isFalse();
    }

    @Test
    void elementContainsString() {
        when(element1.getText()).thenReturn("a");
        assertThat(SeleniumUtils.elementContainsString(element1, "a")).isTrue();
    }

    @Test
    void elementToStringList() {
        when(element1.getText()).thenReturn("abc");
        when(element2.getText()).thenReturn("bcd");
        List<String> result = SeleniumUtils.elementToStringList(Arrays.asList(element1, element2));
        assertThat(result.get(0)).isEqualTo("abc");
        assertThat(result.get(1)).isEqualTo("bcd");
    }

    @Test
    void elementListContainsString() {
        when(element1.getText()).thenReturn("abc");
        when(element2.getText()).thenReturn("bcd");
        List<WebElement> elementList = Arrays.asList(element1, element2);
        assertThat(SeleniumUtils.elementListContainsString(elementList, "abc")).isTrue();
        assertThat(SeleniumUtils.elementListContainsString(elementList, "trf")).isFalse();
    }

    @Test
    void clearOnlyIfNotBlank() {
        when(element1.getAttribute("value")).thenReturn("abc");
        SeleniumUtils.clearInputIfNotBlank(element1);
        Mockito.verify(element1, times(1)).clear();
        when(element2.getAttribute("value")).thenReturn(" ");
        SeleniumUtils.clearInputIfNotBlank(element2);
        Mockito.verify(element2, times(0)).clear();
    }

    @Test
    void clearInputIfNotEqual() {
        when(element1.getText()).thenReturn("abc");
        SeleniumUtils.clearInputIfNotEqual(element1, "def");
        Mockito.verify(element1, times(1)).clear();
        when(element2.getText()).thenReturn("def");
        SeleniumUtils.clearInputIfNotEqual(element2, "def");
        Mockito.verify(element2, times(0)).clear();
    }

    @Test
    void getInputText() {
        String expected = "getInputText";
        when(element1.getAttribute("value")).thenReturn(expected);
        assertThat(SeleniumUtils.getInputText(element1)).isEqualTo(expected);
    }

    @Test
    void isInputBlank() {
        when(element1.getAttribute("value")).thenReturn("isInputBlank");
        assertThat(SeleniumUtils.isInputBlank(element1)).isFalse();
        when(element1.getAttribute("value")).thenReturn(" ");
        assertThat(SeleniumUtils.isInputBlank(element1)).isTrue();
    }

    @Test
    void safeIsVisible() {
        when(element1.getSize()).thenReturn(new Dimension(1, 1));
        assertThat(SeleniumUtils.safeIsVisible(element1)).isTrue();
        when(element1.getSize()).thenReturn(new Dimension(0, 1));
        assertThat(SeleniumUtils.safeIsVisible(element1)).isFalse();
        when(element1.getSize()).thenReturn(new Dimension(1, 0));
        assertThat(SeleniumUtils.safeIsVisible(element1)).isFalse();
        when(element1.getSize()).thenReturn(new Dimension(1, 0));
        assertThat(SeleniumUtils.safeIsVisible(element1)).isFalse();
    }
}
