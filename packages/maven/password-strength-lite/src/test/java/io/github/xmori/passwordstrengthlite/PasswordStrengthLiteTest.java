package io.github.xmori.passwordstrengthlite;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordStrengthLiteTest {
    @Test void longerAndMoreVariedPasswordsScoreHigher() {
        assertTrue(PasswordStrengthLite.analyze("short").score() < PasswordStrengthLite.analyze("aVeryLongPassword123!").score());
        assertEquals(5, PasswordStrengthLite.analyze("aVeryLongPassword123!").score());
        assertEquals(List.of(), PasswordStrengthLite.analyze("aVeryLongPassword123!").advice());
    }

    @Test void reportsAdviceInCheckOrder() {
        PasswordStrengthLite.Result result = PasswordStrengthLite.analyze("correct horse");
        assertEquals(2, result.score());
        assertEquals(List.of("A longer passphrase is stronger", "Mix letter case", "Add a number"), result.advice());
        assertEquals(0, PasswordStrengthLite.analyze("").score());
    }

    @Test void lineBreaksDoNotHideOtherCharacters() {
        assertEquals(PasswordStrengthLite.analyze("abcDEF123!xyzXYZ").score(), PasswordStrengthLite.analyze("\nabcDEF123xyzXYZ").score());
    }

    @Test void countsLengthInCodePoints() {
        assertEquals("Use at least 12 characters", PasswordStrengthLite.analyze("😀".repeat(11)).advice().get(0));
        assertThrows(IllegalArgumentException.class, () -> PasswordStrengthLite.analyze(null));
    }
}
