package no.sanchezrolfsen.framework.selenium.config;

public interface BrowserConfig {

    BrowserType getBrowserType();

    boolean isPrintBrowserLog();

    boolean isRunningRemote();

    String getSeleniumGridAddress();

    void printConfig();
}
