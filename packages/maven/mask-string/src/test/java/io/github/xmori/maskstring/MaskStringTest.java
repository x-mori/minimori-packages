package io.github.xmori.maskstring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MaskStringTest {
    @Test void masksAllButTheLastCharacters() {
        assertEquals("***123", MaskString.of("abc123", 3, '*'));
        assertEquals("************1111", MaskString.of("4111111111111111", 4, '*'));
        assertEquals("######", MaskString.of("secret", 0, '#'));
    }

    @Test void returnsShortInputUnchanged() {
        assertEquals("abc", MaskString.of("abc", 3, '*'));
        assertEquals("abc", MaskString.of("abc", 10, '*'));
        assertEquals("", MaskString.of("", 0, '*'));
    }

    @Test void countsCodePointsNotChars() {
        String emoji = "😀";
        assertEquals("**" + emoji, MaskString.of("ab" + emoji, 1, '*'));
        assertEquals("*b", MaskString.of(emoji + "b", 1, '*'));
    }

    @Test void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> MaskString.of(null, 1, '*'));
        assertThrows(IllegalArgumentException.class, () -> MaskString.of("abc", -1, '*'));
    }
}
