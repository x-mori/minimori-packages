package io.github.xmori.humanduration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HumanDurationTest {
    @Test void formatsNonZeroUnits() {
        assertEquals("1h 2m", HumanDuration.format(3_720_000));
        assertEquals("1m 30s", HumanDuration.format(90_000));
        assertEquals("1d 1h 1m 1s", HumanDuration.format(90_061_000));
        assertEquals("1h 5s", HumanDuration.format(3_605_000));
        assertEquals("400d", HumanDuration.format(400L * 86_400_000));
    }

    @Test void subSecondDurationsAreZeroSeconds() {
        assertEquals("0s", HumanDuration.format(0));
        assertEquals("0s", HumanDuration.format(999));
    }

    @Test void rejectsNegativeDurations() {
        assertThrows(IllegalArgumentException.class, () -> HumanDuration.format(-1));
    }
}
