package no.sanchezrolfsen.framework.selenium.config;

import static java.lang.String.format;
import static no.sanchezrolfsen.framework.selenium.config.BrowserType.*;

public class SeleniumOptions {
    public static BrowserType stringToBrowserType(String browserType) throws IllegalArgumentException {
        return switch (browserType.toUpperCase()) {
            case "CHROME" -> CHROME;
            case "CHROME_HEADLESS" -> CHROME_HEADLESS;
            case "FIREFOX" -> FIREFOX;
            case "FIREFOX_HEADLESS" -> FIREFOX_HEADLESS;
            default -> throw new IllegalArgumentException(format("Browser '%s' is not supported in the framework", browserType));
        };
    }
}
