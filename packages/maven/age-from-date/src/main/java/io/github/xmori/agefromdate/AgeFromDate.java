package io.github.xmori.agefromdate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Calculates a person's age in completed years from a birth date.
 *
 * <pre>{@code
 * int age = AgeFromDate.years(LocalDate.of(1990, 7, 15), LocalDate.now(ZoneId.of("Asia/Tokyo")));
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class AgeFromDate {
    private AgeFromDate() {}

    /**
     * Returns the number of completed years between a birth date and a reference date.
     *
     * <p>The calculation uses calendar dates, so a birthday later in the reference
     * year does not count yet, and a birth date equal to {@code today} gives 0. For
     * someone born on 29 February, the birthday is reached on 1 March in common
     * years, matching {@link java.time.Period#between}.
     *
     * <p>The caller supplies {@code today} so the time zone is explicit; pass
     * {@code LocalDate.now(zone)} for the current age in a given zone.
     *
     * @param birthDate date of birth
     * @param today date on which to measure the age
     * @return completed years, zero or greater
     * @throws IllegalArgumentException if either date is null or birthDate is after today
     */
    public static int years(LocalDate birthDate, LocalDate today) {
        if (birthDate == null || today == null || birthDate.isAfter(today)) throw new IllegalArgumentException("invalid birth date");
        return Math.toIntExact(ChronoUnit.YEARS.between(birthDate, today));
    }
}
