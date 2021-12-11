package selenium.rest;

import selenium.config.BrowserConfig;

import java.time.format.DateTimeFormatter;

public interface Config {
    String getBaseUrl();

    String getSeleniumGridUrl();

    BrowserConfig getBrowserConfig();

    void printConfig();

    DateTimeFormatter getStandardDateFormat();
}
