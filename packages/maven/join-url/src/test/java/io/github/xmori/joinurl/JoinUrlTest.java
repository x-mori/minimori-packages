package io.github.xmori.joinurl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JoinUrlTest {
    @Test void joinsWithSingleSlashes() {
        assertEquals("https://example.com/a/b", JoinUrl.join("https://example.com/a/", "/b/"));
        assertEquals("https://api.example.com/v1/users/42", JoinUrl.join("https://api.example.com/v1/", "/users/", "42"));
        assertEquals("https://example.com/x", JoinUrl.join("https://example.com", "x"));
        assertEquals("https://example.com/a/", JoinUrl.join("https://example.com/a/"));
    }

    @Test void skipsEmptySegmentsAndKeepsQueryAndFragment() {
        assertEquals("https://example.com/docs?lang=en#top", JoinUrl.join("https://example.com?lang=en#top", "", "//", "docs"));
        assertEquals("http://localhost:8080/a/b", JoinUrl.join("http://localhost:8080", "a/b"));
    }

    @Test void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> JoinUrl.join("/relative", "x"));
        assertThrows(IllegalArgumentException.class, () -> JoinUrl.join("https://user:pw@example.com", "x"));
        assertThrows(IllegalArgumentException.class, () -> JoinUrl.join("https://example.com", (String) null));
        assertThrows(IllegalArgumentException.class, () -> JoinUrl.join("https://example.com", "has space"));
        assertThrows(IllegalArgumentException.class, () -> JoinUrl.join(" "));
    }
}
