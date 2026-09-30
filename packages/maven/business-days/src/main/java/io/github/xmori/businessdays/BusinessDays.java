package io.github.xmori.businessdays;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Counts weekdays (Monday through Friday) in a date range.
 *
 * <pre>{@code
 * long days = BusinessDays.count(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 16)); // 10
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class BusinessDays {
    private BusinessDays() {}

    /**
     * Counts Monday through Friday in an inclusive date range.
     *
     * <p>Both {@code start} and {@code end} are counted when they are weekdays, so a
     * single weekday gives 1. The count runs in constant time for any range length:
     * complete weeks contribute five days each, and at most six remaining days are
     * checked. Public holidays are not excluded; subtract them separately if needed.
     *
     * @param start first date in the range, included
     * @param end last date in the range, included
     * @return the number of weekdays in the range
     * @throws IllegalArgumentException if either date is null or start is after end
     */
    public static long count(LocalDate start, LocalDate end) {
        if (start == null || end == null || start.isAfter(end)) throw new IllegalArgumentException("invalid range");
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        long weekdays = days / 7 * 5;
        DayOfWeek day = start.getDayOfWeek();
        for (long i = days % 7; i > 0; i--, day = day.plus(1)) {
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) weekdays++;
        }
        return weekdays;
    }
}
