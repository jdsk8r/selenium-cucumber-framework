package no.sanchezrolfsen.framework.selenium;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class TestUtilsTest {

    @Test
    void addThousandSeparator() {
        assertThat(TestUtils.addThousandSeparator("1234567")).isEqualTo("1 234 567");
    }

    @Test
    void doubleFromString() {
        assertThat(TestUtils.doubleFromString("100.99")).isEqualTo(100.99);
    }

    @Test
    void longFromString() {
        assertThat(TestUtils.longFromString("999 999 999 999")).isEqualTo(999999999999L);
    }

    @Test
    void integerFromString() {
        assertThat(TestUtils.integerFromString("199")).isEqualTo(199);
    }

    @Test
    void extractIntegerFromString() {
        assertThat(TestUtils.extractIntegerFromString("114,045")).isEqualTo(114045);
        assertThat(TestUtils.extractIntegerFromString("$123")).isEqualTo(123);
        assertThat(TestUtils.extractIntegerFromString("99 Kr")).isEqualTo(99);
    }

    @Test
    void formatAsURI() {
        assertThat(TestUtils.formatAsURI("hostname", 8080, "test1", true)).isEqualTo("https://hostname:8080/test1");
        assertThat(TestUtils.formatAsURI("hostname", 8080, "test2", false)).isEqualTo("http://hostname:8080/test2");
    }

    @Test
    void testFormatAsURI() {
        assertThat(TestUtils.formatAsURI("localhost", 3032, "test2")).isEqualTo("http://localhost:3032/test2");
    }

    @Test
    void removeWhitespace() {
        assertThat(TestUtils.removeWhitespace("Lorem Ipsum is simply dummy text")).isEqualTo("LoremIpsumissimplydummytext");
    }

    @Test
    void removeNonNumeric() {
        assertThat(TestUtils.removeNonNumeric("1adasd334412asd1")).isEqualTo("13344121");
    }

    @Test
    void intToMonth() {
        assertThat(TestUtils.intToMonth(6, Locale.US)).isEqualTo("June");
        assertThat(TestUtils.intToMonth(10, Locale.US)).isEqualTo("October");
        assertThat(TestUtils.intToMonth(6, new Locale("nb"))).isEqualTo("juni");
        assertThat(TestUtils.intToMonth(10, new Locale("nb"))).isEqualTo("oktober");
    }
}