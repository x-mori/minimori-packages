package io.github.xmori.securepin;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SecurePinTest {
    @Test void generatesExactlyTheRequestedDigits() {
        assertTrue(SecurePin.generate(6).matches("[0-9]{6}"));
        assertTrue(SecurePin.generate(1024).matches("[0-9]{1024}"));
    }

    @Test void usesEveryDigit() {
        Set<Character> seen = new HashSet<>();
        for (char c : SecurePin.generate(1024).toCharArray()) seen.add(c);
        assertEquals(10, seen.size());
    }

    @Test void rejectsLengthsOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> SecurePin.generate(0));
        assertThrows(IllegalArgumentException.class, () -> SecurePin.generate(1025));
    }
}
