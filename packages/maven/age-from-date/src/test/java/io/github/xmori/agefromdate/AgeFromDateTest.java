package io.github.xmori.agefromdate;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgeFromDateTest {
    @Test void countsCompletedYears() {
        assertEquals(17, AgeFromDate.years(LocalDate.of(2008, 12, 1), LocalDate.of(2026, 1, 1)));
        assertEquals(18, AgeFromDate.years(LocalDate.of(2008, 1, 1), LocalDate.of(2026, 1, 1)));
        assertEquals(0, AgeFromDate.years(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 1)));
    }

    @Test void leapDayBirthdayArrivesOnFirstOfMarch() {
        LocalDate born = LocalDate.of(2000, 2, 29);
        assertEquals(0, AgeFromDate.years(born, LocalDate.of(2001, 2, 28)));
        assertEquals(1, AgeFromDate.years(born, LocalDate.of(2001, 3, 1)));
    }

    @Test void rejectsFutureAndNullDates() {
        assertThrows(IllegalArgumentException.class, () -> AgeFromDate.years(LocalDate.of(2030, 1, 1), LocalDate.of(2026, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> AgeFromDate.years(null, LocalDate.of(2026, 1, 1)));
    }
}
