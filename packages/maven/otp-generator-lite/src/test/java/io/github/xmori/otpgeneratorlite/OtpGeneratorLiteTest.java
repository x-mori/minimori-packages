package io.github.xmori.otpgeneratorlite;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OtpGeneratorLiteTest {
    private static final byte[] RFC_SECRET = "12345678901234567890".getBytes(StandardCharsets.US_ASCII);

    @Test void matchesRfc6238Sha1Vectors() {
        long[] times = {59, 1_111_111_109, 1_111_111_111, 1_234_567_890, 2_000_000_000, 20_000_000_000L};
        String[] codes = {"94287082", "07081804", "14050471", "89005924", "69279037", "65353130"};
        for (int i = 0; i < times.length; i++) {
            assertEquals(codes[i], OtpGeneratorLite.generate(RFC_SECRET, Instant.ofEpochSecond(times[i]), 8, 30));
        }
    }

    @Test void truncatesToRequestedDigitsWithZeroPadding() {
        assertEquals("287082", OtpGeneratorLite.generate(RFC_SECRET, Instant.ofEpochSecond(59), 6, 30));
        assertEquals("081804", OtpGeneratorLite.generate(RFC_SECRET, Instant.ofEpochSecond(1_111_111_109), 6, 30));
    }

    @Test void sameStepGivesSameCode() {
        assertEquals(OtpGeneratorLite.generate(RFC_SECRET, Instant.ofEpochSecond(60), 6, 30), OtpGeneratorLite.generate(RFC_SECRET, Instant.ofEpochSecond(89), 6, 30));
    }

    @Test void rejectsInvalidSettings() {
        assertThrows(IllegalArgumentException.class, () -> OtpGeneratorLite.generate(new byte[0], Instant.EPOCH, 6, 30));
        assertThrows(IllegalArgumentException.class, () -> OtpGeneratorLite.generate(RFC_SECRET, Instant.EPOCH, 5, 30));
        assertThrows(IllegalArgumentException.class, () -> OtpGeneratorLite.generate(RFC_SECRET, Instant.EPOCH, 10, 30));
        assertThrows(IllegalArgumentException.class, () -> OtpGeneratorLite.generate(RFC_SECRET, Instant.EPOCH, 6, 0));
        assertThrows(IllegalArgumentException.class, () -> OtpGeneratorLite.generate(RFC_SECRET, null, 6, 30));
    }
}
