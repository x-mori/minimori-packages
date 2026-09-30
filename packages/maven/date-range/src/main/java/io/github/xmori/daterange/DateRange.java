package io.github.xmori.daterange;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates evenly spaced dates by day, week, or month.
 *
 * <pre>{@code
 * List<LocalDate> ends = DateRange.between(LocalDate.of(2026, 1, 31), LocalDate.of(2026, 4, 30), ChronoUnit.MONTHS);
 * // [2026-01-31, 2026-02-28, 2026-03-31, 2026-04-30]
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class DateRange {
    private DateRange() {}

    /**
     * Generates dates from {@code start} to {@code end}, inclusive, at a fixed step.
     *
     * <p>The n-th date is {@code start.plus(n, unit)}, always computed from
     * {@code start}. For {@link ChronoUnit#MONTHS} this keeps the original day of
     * month where it exists: a range starting on 31 January gives 28 or 29
     * February, then 31 March, rather than drifting to the 28th. The start date is
     * always included; the end date appears only when it falls on a step.
     *
     * <p>The whole list is built in memory, so a daily range across many centuries
     * holds one entry per day.
     *
     * @param start first date
     * @param end inclusive upper bound
     * @param unit {@link ChronoUnit#DAYS}, {@link ChronoUnit#WEEKS}, or {@link ChronoUnit#MONTHS}
     * @return an unmodifiable list of dates in ascending order
     * @throws IllegalArgumentException if an argument is null, start is after end, or unit is unsupported
     */
    public static List<LocalDate> between(LocalDate start, LocalDate end, ChronoUnit unit) {
        if (start == null || end == null || start.isAfter(end) || !(unit == ChronoUnit.DAYS || unit == ChronoUnit.WEEKS || unit == ChronoUnit.MONTHS)) throw new IllegalArgumentException("invalid range");
        // unit.between can undercount by one when a month-end clamp still lands in range.
        List<LocalDate> dates = new ArrayList<>((int) Math.min(unit.between(start, end) + 2, Integer.MAX_VALUE - 8));
        for (long i = 0; ; i++) {
            LocalDate date = start.plus(i, unit);
            if (date.isAfter(end)) break;
            dates.add(date);
        }
        return List.copyOf(dates);
    }
}
