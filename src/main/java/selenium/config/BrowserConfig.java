package selenium.config;

public interface BrowserConfig {

    SeleniumOptions.BrowserType getBrowserType();

    boolean isPrintBrowserLog();

    boolean isRunningRemote();

    String getSeleniumGridAddress();

    void printConfig();
}
