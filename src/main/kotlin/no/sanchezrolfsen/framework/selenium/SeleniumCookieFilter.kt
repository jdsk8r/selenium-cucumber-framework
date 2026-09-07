package no.sanchezrolfsen.framework.selenium

import no.sanchezrolfsen.framework.selenium.rest.CookieRetriever

import javax.ws.rs.client.ClientRequestContext
import javax.ws.rs.client.ClientRequestFilter

class SeleniumCookieFilter(private val cookieRetriever: CookieRetriever) : ClientRequestFilter {

    /** Record-style accessor, kept for API compatibility with the original Java record. */
    fun cookieRetriever(): CookieRetriever = cookieRetriever

    override fun filter(requestContext: ClientRequestContext) {
        val cookie = cookieRetriever.getCookie()
        requestContext.headers.add("Cookie", "${cookie.name}=${cookie.value}")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SeleniumCookieFilter) return false
        return cookieRetriever == other.cookieRetriever
    }

    override fun hashCode(): Int = cookieRetriever.hashCode()

    override fun toString(): String = "SeleniumCookieFilter[cookieRetriever=$cookieRetriever]"
}
