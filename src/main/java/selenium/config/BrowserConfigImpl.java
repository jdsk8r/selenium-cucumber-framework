package selenium.config;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Data @Slf4j
public class BrowserConfigImpl implements BrowserConfig {

    private SeleniumOptions.BrowserType browserType;
    private boolean printBrowserLog;
    private String seleniumGridAddress;

    public BrowserConfigImpl() {
        browserType = SeleniumOptions.BrowserType.CHROME;
    }

    public void setSeleniumGridAddress(String address) {
        if (isNotBlank(address)) {
            this.seleniumGridAddress = address;
        } else {
            log.debug("The address to selenium grid is empty it will be set to null");
            this.seleniumGridAddress = null;
        }
    }

    public boolean isRunningRemote() {
        return isNotBlank(seleniumGridAddress);
    }

    @Override
    public void printConfig() {
        log.info("==========================================================================");
        log.info("Selenium-configuration");
        log.info("--------------------------------------------------------------------------");
        log.info("Browsertype:         {}", browserType.name());
        log.info("Running type:          {}", isRunningRemote() ? "Remote" : "Local");
        log.info("Print browserlog:   {}", printBrowserLog ? "Yes" : "No");
        if (isRunningRemote()) log.info("Selenium Grid-address: {}", seleniumGridAddress);
        log.info("==========================================================================");
    }

    @Builder(builderMethodName = "with")
    public static BrowserConfigImpl builder(SeleniumOptions.BrowserType browserType,
                                            boolean printBrowserLog, String seleniumGridAddress) {
        BrowserConfigImpl config = new BrowserConfigImpl();
        config.setSeleniumGridAddress(seleniumGridAddress);
        config.setPrintBrowserLog(printBrowserLog);
        if (browserType == null) browserType = SeleniumOptions.BrowserType.CHROME;
        config.setBrowserType(browserType);
        return config;
    }
}
