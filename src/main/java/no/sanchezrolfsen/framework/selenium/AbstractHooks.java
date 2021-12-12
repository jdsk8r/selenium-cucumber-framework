package no.sanchezrolfsen.framework.selenium;

import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import no.sanchezrolfsen.framework.selenium.config.BrowserConfig;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public abstract class AbstractHooks {
    public static final boolean runBeforeAll = true;
    public static final boolean exitHard = true;
    public static boolean exitTestRun = false;

    public void beforeEach() {
        if (runBeforeAll) {
            beforeAll();
        }
    }

    public abstract void beforeAll();

    public abstract BrowserConfig getBrowserConfig();

    public void afterFailedScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            SeleniumUtils.embedScreenshot(scenario);
            String fileName = String.format("%s_%s.png",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd_HH-mm")),
                    scenario.getName().replaceAll("[^\\w.-]", "_"));
            SeleniumUtils.saveScreenshot(scenario, "target" + File.separator + "screenshots", fileName);
            if (getBrowserConfig().isPrintBrowserLog()) SeleniumUtils.printBrowserLog();
        }
    }

    public void unexpectedShutdown(String errorMessage) {
        log.warn(errorMessage);
        if (exitHard) Runtime.getRuntime().exit(0);
        exitTestRun = true;
    }
}
