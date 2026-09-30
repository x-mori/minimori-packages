package io.github.xmori.randomstringsecure;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RandomStringSecureTest {
    @Test void drawsFromTheAlphabet() {
        assertTrue(RandomStringSecure.generate(16, "ab").matches("[ab]{16}"));
        assertTrue(RandomStringSecure.generate(64, "0123456789abcdef").matches("[0-9a-f]{64}"));
    }

    @Test void treatsSupplementaryCharactersAsOneCodePoint() {
        String result = RandomStringSecure.generate(10, "😀😁");
        assertEquals(10, result.codePointCount(0, result.length()));
    }

    @Test void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringSecure.generate(0, "ab"));
        assertThrows(IllegalArgumentException.class, () -> RandomStringSecure.generate(1025, "ab"));
        assertThrows(IllegalArgumentException.class, () -> RandomStringSecure.generate(4, "aa"));
        assertThrows(IllegalArgumentException.class, () -> RandomStringSecure.generate(4, null));
    }
}
