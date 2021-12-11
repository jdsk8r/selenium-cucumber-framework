package selenium;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import static java.lang.String.format;

@Slf4j
@UtilityClass
public class TestUtils {

    public String addThousandSeparator(String n) {
        return n.replaceAll("(?<=\\d)(?=(\\d\\d\\d)+(?!\\d))", " ");
    }

    public static Double doubleFromString(String string) {
        String numberString = verifyAndFormat(string, false);
        if (numberString == null) {
            return null;
        }
        return Double.valueOf(numberString);
    }

    public Long longFromString(String string) {
        String numberString = verifyAndFormat(string, false);
        if (numberString == null) {
            return null;
        }
        return Long.valueOf(numberString);
    }

    public static Integer integerFromString(String string) {
        String numberString = verifyAndFormat(string, false);
        if (numberString == null) {
            return null;
        }
        return Integer.valueOf(numberString);
    }

    public Integer extractIntegerFromString(final String string) {
        String numberString = verifyAndFormat(string, true);
        if (numberString == null) {
            return null;
        }
        return Integer.valueOf(numberString);
    }

    private String verifyAndFormat(final String string, boolean ignoreNonNumeric) {
        if (string == null) return null;
        String formatted = ignoreNonNumeric ? removeNonNumeric(string) : removeWhitespace(string);
        if (NumberUtils.isParsable(formatted)) {
            return formatted;
        } else {
            return null;
        }
    }

    public String formatAsURI(final String host, final int port, final String path, final boolean useHTTPS) {
        String protocol = useHTTPS ? "https" : "http";
        String portString;
        if ((useHTTPS && port == 443) || (!useHTTPS && port == 80)) portString = "";
        else portString = format(":%d", port);
        return format("%s://%s%s/%s", protocol, host, portString, path);
    }

    public String formatAsURI(final String host, final int port, final String path) {
        return formatAsURI(host, port, path, false);
    }

    public String removeWhitespace(@NonNull String string) {
        return string.replaceAll("\\s+", "");
    }

    public String removeNonNumeric(@NonNull String string) {
        return string.replaceAll("\\D+", "");
    }

    public String intToMonth(int month, Locale locale) {
        return Month.of(month).getDisplayName(TextStyle.FULL, locale);
    }
}
