package io.github.xmori.randomidlite;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RandomIdLiteTest {
    @Test void usesOnlyTheReadableAlphabet() {
        assertEquals(12, RandomIdLite.generate(12).length());
        assertTrue(RandomIdLite.generate(1024).matches("[23456789ABCDEFGHJKLMNPQRSTUVWXYZ]{1024}"));
    }

    @Test void coversEveryAlphabetCharacter() {
        Set<Character> seen = new HashSet<>();
        for (char c : RandomIdLite.generate(1024).toCharArray()) seen.add(c);
        assertEquals(32, seen.size());
    }

    @Test void rejectsLengthsOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> RandomIdLite.generate(0));
        assertThrows(IllegalArgumentException.class, () -> RandomIdLite.generate(1025));
    }
}
