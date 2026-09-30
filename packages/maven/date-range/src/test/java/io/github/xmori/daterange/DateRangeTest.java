package io.github.xmori.daterange;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DateRangeTest {
    @Test void generatesDaysAndWeeks() {
        assertEquals(3, DateRange.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 3), ChronoUnit.DAYS).size());
        assertEquals(List.of(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 8)), DateRange.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 14), ChronoUnit.WEEKS));
    }

    @Test void monthStepsDoNotDriftFromMonthEnd() {
        List<LocalDate> expected = List.of(LocalDate.of(2026, 1, 31), LocalDate.of(2026, 2, 28), LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 30));
        assertEquals(expected, DateRange.between(LocalDate.of(2026, 1, 31), LocalDate.of(2026, 4, 30), ChronoUnit.MONTHS));
    }

    @Test void resultIsUnmodifiable() {
        List<LocalDate> dates = DateRange.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 1), ChronoUnit.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> dates.add(LocalDate.of(2026, 1, 2)));
    }

    @Test void rejectsUnsupportedUnitAndReversedRange() {
        assertThrows(IllegalArgumentException.class, () -> DateRange.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1), ChronoUnit.HOURS));
        assertThrows(IllegalArgumentException.class, () -> DateRange.between(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 1), ChronoUnit.DAYS));
    }
}
