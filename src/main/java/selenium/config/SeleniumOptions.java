package selenium.config;

import static java.lang.String.format;
import static selenium.config.SeleniumOptions.BrowserType.*;

public class SeleniumOptions {
    public enum BrowserType {
        CHROME, CHROME_HEADLESS, FIREFOX, FIREFOX_HEADLESS,
    }

    public static BrowserType stringToBrowserType(String browserType) throws IllegalArgumentException {
        return switch (browserType) {
            case "CHROME" -> CHROME;
            case "CHROME_HEADLESS" -> CHROME_HEADLESS;
            case "FIREFOX" -> FIREFOX;
            case "FIREFOX_HEADLESS" -> FIREFOX_HEADLESS;
            default -> throw new IllegalArgumentException(format("Browser '%s' is not supported in the framework", browserType));
        };
    }
}
