package no.sanchezrolfsen.framework.selenium

import org.openqa.selenium.Cookie
import org.slf4j.LoggerFactory

class CookieStore(cookieSet: Set<Cookie>) {

    private val log = LoggerFactory.getLogger(CookieStore::class.java)

    private val cookieSet: Set<Cookie> = HashSet(cookieSet)

    init {
        log.info("Setter cookies: $cookieSet")
    }

    companion object {
        @JvmStatic
        fun toString(cookieSet: Set<Cookie>): String =
            cookieSet.joinToString("; ") { "${it.name}=${it.value}" }
    }

    /** Record-style accessor, kept for API compatibility with the original Java record. */
    fun cookieSet(): Set<Cookie> = cookieSet

    fun asCookieSet(): Set<Cookie> = cookieSet

    fun asString(): String = toString(cookieSet)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CookieStore) return false
        return cookieSet == other.cookieSet
    }

    override fun hashCode(): Int = cookieSet.hashCode()

    override fun toString(): String = "CookieStore[cookieSet=$cookieSet]"
}
