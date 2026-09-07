package no.sanchezrolfsen.framework.selenium.rest

import org.openqa.selenium.Cookie

interface CookieRetriever {
    fun getCookie(): Cookie
}
