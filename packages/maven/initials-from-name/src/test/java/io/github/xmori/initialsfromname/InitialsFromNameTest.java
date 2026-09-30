package io.github.xmori.initialsfromname;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InitialsFromNameTest {
    @Test void takesTheFirstLetterOfEachWord() {
        assertEquals("JS", InitialsFromName.of("John Smith"));
        assertEquals("JP", InitialsFromName.of("  jean-luc   picard "));
        assertEquals("ÉZ", InitialsFromName.of("émile zola"));
    }

    @Test void splitsOnAnyUnicodeWhitespace() {
        assertEquals("JS", InitialsFromName.of("John\tSmith"));
        assertEquals("JS", InitialsFromName.of("John\nSmith"));
        assertEquals("JS", InitialsFromName.of("John Smith"));
        assertEquals("JS", InitialsFromName.of("John　Smith"));
    }

    @Test void keepsSupplementaryCharactersWhole() {
        assertEquals("𝐀B", InitialsFromName.of("𝐀lpha beta"));
    }

    @Test void blankNamesGiveEmptyInitials() {
        assertEquals("", InitialsFromName.of(""));
        assertEquals("", InitialsFromName.of(" \t "));
        assertThrows(IllegalArgumentException.class, () -> InitialsFromName.of(null));
    }
}
