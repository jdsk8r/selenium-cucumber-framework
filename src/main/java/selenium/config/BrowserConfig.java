package selenium.config;

public interface BrowserConfig {

    BrowserType getBrowserType();

    boolean isPrintBrowserLog();

    boolean isRunningRemote();

    String getSeleniumGridAddress();

    void printConfig();
}
