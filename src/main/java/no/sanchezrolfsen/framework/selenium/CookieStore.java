package no.sanchezrolfsen.framework.selenium;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
public record CookieStore(Set<Cookie> cookieSet) {
    public CookieStore(Set<Cookie> cookieSet) {
        log.info("Setter cookies: " + cookieSet.toString());
        this.cookieSet = new HashSet<>(cookieSet);
    }

    public static String toString(Set<Cookie> cookieSet) {
        return cookieSet.stream()
                .map(cookie -> format("%s=%s", cookie.getName(), cookie.getValue()))
                .collect(Collectors.joining("; "));
    }

    public Set<Cookie> asCookieSet() {
        return cookieSet;
    }

    public String asString() {
        return toString(cookieSet);
    }
}
