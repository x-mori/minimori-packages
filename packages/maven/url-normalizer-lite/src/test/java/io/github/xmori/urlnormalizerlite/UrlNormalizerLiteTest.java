package io.github.xmori.urlnormalizerlite;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UrlNormalizerLiteTest {
    @Test void lowercasesSchemeAndHostAndDropsDefaultPorts() {
        assertEquals("https://example.com/a", UrlNormalizerLite.normalize("HTTPS://EXAMPLE.COM:443/a"));
        assertEquals("http://example.com/", UrlNormalizerLite.normalize("http://example.com:80"));
        assertEquals("http://example.com:8080/", UrlNormalizerLite.normalize("http://example.com:8080"));
    }

    @Test void resolvesDotSegmentsAndKeepsQueryAndEscapes() {
        assertEquals("https://example.com/a/c?q=1&B=%2f#Frag", UrlNormalizerLite.normalize("HTTPS://Example.COM:443/a/./b/../c?q=1&B=%2f#Frag"));
    }

    @Test void keepsIpv6BracketsSingle() {
        assertEquals("http://[::1]/a", UrlNormalizerLite.normalize("http://[::1]:80/a"));
        assertEquals("http://[::1]:8080/", UrlNormalizerLite.normalize("http://[::1]:8080"));
    }

    @Test void rejectsUnsupportedUrls() {
        assertThrows(IllegalArgumentException.class, () -> UrlNormalizerLite.normalize(null));
        assertThrows(IllegalArgumentException.class, () -> UrlNormalizerLite.normalize("ftp://example.com"));
        assertThrows(IllegalArgumentException.class, () -> UrlNormalizerLite.normalize("https://user:pw@example.com"));
        assertThrows(IllegalArgumentException.class, () -> UrlNormalizerLite.normalize("/relative"));
        assertThrows(IllegalArgumentException.class, () -> UrlNormalizerLite.normalize("https://exa mple.com"));
    }
}
