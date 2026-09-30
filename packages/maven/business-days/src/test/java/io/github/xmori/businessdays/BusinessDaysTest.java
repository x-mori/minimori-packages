package io.github.xmori.businessdays;

import java.time.DayOfWeek;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusinessDaysTest {
    @Test void countsWeekdaysInclusively() {
        assertEquals(5, BusinessDays.count(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 11)));
        assertEquals(10, BusinessDays.count(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 16)));
        assertEquals(1, BusinessDays.count(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 5)));
        assertEquals(0, BusinessDays.count(LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 11)));
    }

    @Test void matchesDayByDayCountForEveryStartAndLength() {
        LocalDate first = LocalDate.of(2026, 1, 1);
        for (int offset = 0; offset < 7; offset++) {
            LocalDate start = first.plusDays(offset);
            long expected = 0;
            for (int length = 0; length < 40; length++) {
                DayOfWeek day = start.plusDays(length).getDayOfWeek();
                if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) expected++;
                assertEquals(expected, BusinessDays.count(start, start.plusDays(length)));
            }
        }
    }

    @Test void rejectsReversedRange() {
        assertThrows(IllegalArgumentException.class, () -> BusinessDays.count(LocalDate.of(2026, 1, 2), LocalDate.of(2026, 1, 1)));
    }
}
