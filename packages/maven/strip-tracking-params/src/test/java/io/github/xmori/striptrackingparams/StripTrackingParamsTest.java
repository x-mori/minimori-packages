package io.github.xmori.striptrackingparams;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StripTrackingParamsTest {
    @Test void removesTrackingParameters() {
        assertEquals("https://example.com/?q=x", StripTrackingParams.strip("https://example.com/?utm_source=y&q=x"));
        assertEquals("https://example.com/post?id=7#top", StripTrackingParams.strip("https://example.com/post?id=7&utm_source=news&fbclid=abc#top"));
        assertEquals("https://example.com/", StripTrackingParams.strip("https://example.com/?UTM_Medium=a&GCLID=b&msclkid"));
    }

    @Test void keepsOtherParametersExactly() {
        assertEquals("https://example.com/?a=%20&b=1+2&utm=keep", StripTrackingParams.strip("https://example.com/?a=%20&utm_x=1&b=1+2&utm=keep"));
        assertEquals("https://example.com/a#f?x", StripTrackingParams.strip("https://example.com/a#f?x"));
        assertEquals("https://example.com/", StripTrackingParams.strip("https://example.com/"));
    }

    @Test void rejectsInvalidUrls() {
        assertThrows(IllegalArgumentException.class, () -> StripTrackingParams.strip(null));
        assertThrows(IllegalArgumentException.class, () -> StripTrackingParams.strip("/relative?utm_source=x"));
        assertThrows(IllegalArgumentException.class, () -> StripTrackingParams.strip("https://exa mple.com"));
    }
}
