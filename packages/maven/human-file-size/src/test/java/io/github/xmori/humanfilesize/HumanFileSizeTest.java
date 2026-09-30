package io.github.xmori.humanfilesize;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HumanFileSizeTest {
    @Test void formatsSiAndBinaryUnits() {
        assertEquals("1.5 KB", HumanFileSize.format(1_500, false));
        assertEquals("1.5 MiB", HumanFileSize.format(1_572_864, true));
        assertEquals("999 B", HumanFileSize.format(999, false));
        assertEquals("1000 B", HumanFileSize.format(1_000, true));
        assertEquals("1.0 KiB", HumanFileSize.format(1_024, true));
    }

    @Test void roundingMovesToTheNextUnit() {
        assertEquals("999.9 KB", HumanFileSize.format(999_949, false));
        assertEquals("1.0 MB", HumanFileSize.format(999_950, false));
        assertEquals("1.0 MiB", HumanFileSize.format(1_048_575, true));
    }

    @Test void handlesTheLargestLongValue() {
        assertEquals("9.2 EB", HumanFileSize.format(Long.MAX_VALUE, false));
        assertEquals("8.0 EiB", HumanFileSize.format(Long.MAX_VALUE, true));
    }

    @Test void rejectsNegativeSizes() {
        assertThrows(IllegalArgumentException.class, () -> HumanFileSize.format(-1, false));
    }
}
