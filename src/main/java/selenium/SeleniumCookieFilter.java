package selenium;

import org.openqa.selenium.Cookie;
import selenium.rest.CookieRetriever;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import static java.lang.String.format;

public record SeleniumCookieFilter(CookieRetriever cookieRetriever) implements ClientRequestFilter {

    public void filter(ClientRequestContext requestContext) {
        Cookie cookie = cookieRetriever.getCookie();
        requestContext.getHeaders().add("Cookie", format("%s=%s", cookie.getName(), cookie.getValue()));
    }
}
