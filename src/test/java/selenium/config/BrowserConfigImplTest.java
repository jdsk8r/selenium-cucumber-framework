package selenium.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test of browser configuration")
public class BrowserConfigImplTest {
    @Test
    @DisplayName("Test that the setup is correct for the default constructor")
    void standardParameterEmptyConstructor() {
        BrowserConfig browserConfig = new BrowserConfigImpl();
        assertThat(browserConfig.isRunningRemote()).isFalse();
        assertThat(browserConfig.getSeleniumGridAddress()).isNull();
        assertThat(browserConfig.getBrowserType()).isEqualTo(SeleniumOptions.BrowserType.CHROME);
        assertThat(browserConfig.isPrintBrowserLog()).isFalse();
    }

    @Test
    @DisplayName("Test that the setup is correct for empty builder")
    void standardParameterEmptyBuilder() {
        BrowserConfig browserConfig = BrowserConfigImpl.with().build();
        assertThat(browserConfig.isRunningRemote()).isFalse();
        assertThat(browserConfig.getSeleniumGridAddress()).isNull();
        assertThat(browserConfig.getBrowserType()).isEqualTo(SeleniumOptions.BrowserType.CHROME);
        assertThat(browserConfig.isPrintBrowserLog()).isFalse();
    }

    @Test
    @DisplayName("setSeleniumGridAddress is not set if empty")
    void setSeleniumGridAddress() {
        final String blankWhitespace = " ";
        BrowserConfigImpl browserConfig = new BrowserConfigImpl();
        browserConfig.setSeleniumGridAddress(blankWhitespace);
        assertThat(browserConfig.getSeleniumGridAddress()).isNull();
        assertThat(browserConfig.isRunningRemote()).isFalse();
    }

    @Test
    @DisplayName("setSeleniumGridAddress sets runningRemote to true if selenium grid is not empty")
    void setSeleniumGridAddressBuilder() {
        final String blankWhitespace = " ";
        final String address = "address";
        BrowserConfigImpl browserConfig = BrowserConfigImpl.with().seleniumGridAddress(address).build();
        assertThat(browserConfig.isRunningRemote()).isTrue();
        assertThat(browserConfig.getSeleniumGridAddress()).isEqualTo(address);

        browserConfig = BrowserConfigImpl.with().seleniumGridAddress(blankWhitespace).build();
        assertThat(browserConfig.isRunningRemote()).isFalse();
        assertThat(browserConfig.getSeleniumGridAddress()).isNull();

        browserConfig = BrowserConfigImpl.with().seleniumGridAddress(null).build();
        assertThat(browserConfig.isRunningRemote()).isFalse();
        assertThat(browserConfig.getSeleniumGridAddress()).isNull();
    }

    @Test
    @DisplayName("RemoteRunning specified afterwards it sets remote running to true")
    void setSeleniumGridAddressBuilderOgMethod() {
        final String address = "address";
        BrowserConfigImpl browserConfig = BrowserConfigImpl.with().build();
        assertThat(browserConfig.isRunningRemote()).isFalse();

        browserConfig.setSeleniumGridAddress(address);
        assertThat(browserConfig.getSeleniumGridAddress()).isEqualTo(address);
        assertThat(browserConfig.isRunningRemote()).isTrue();
    }
}
