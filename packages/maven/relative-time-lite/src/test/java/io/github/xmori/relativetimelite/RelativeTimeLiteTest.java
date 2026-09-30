package io.github.xmori.relativetimelite;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RelativeTimeLiteTest {
    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test void describesPastAndFuture() {
        assertEquals("5 minutes ago", RelativeTimeLite.format(Instant.EPOCH, Instant.EPOCH.plusSeconds(300)));
        assertEquals("in 2 days", RelativeTimeLite.format(NOW.plusSeconds(2 * 86_400 + 5), NOW));
        assertEquals("1 hour ago", RelativeTimeLite.format(NOW.minusSeconds(3_600), NOW));
        assertEquals("in 0 seconds", RelativeTimeLite.format(NOW, NOW));
    }

    @Test void roundsDownToWholeUnits() {
        assertEquals("1 minute ago", RelativeTimeLite.format(NOW.minusSeconds(119), NOW));
        assertEquals("59 seconds ago", RelativeTimeLite.format(NOW.minusSeconds(59), NOW));
        assertEquals("0 seconds ago", RelativeTimeLite.format(NOW.minusMillis(500), NOW));
    }

    @Test void rejectsNullInstants() {
        assertThrows(IllegalArgumentException.class, () -> RelativeTimeLite.format(null, NOW));
        assertThrows(IllegalArgumentException.class, () -> RelativeTimeLite.format(NOW, null));
    }
}
