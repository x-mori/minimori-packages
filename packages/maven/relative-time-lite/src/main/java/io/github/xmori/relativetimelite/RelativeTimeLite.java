package io.github.xmori.relativetimelite;

import java.time.Duration;
import java.time.Instant;

/**
 * Describes an instant relative to another in short English text such as
 * {@code 5 minutes ago} or {@code in 2 days}.
 *
 * <pre>{@code
 * RelativeTimeLite.format(comment.createdAt(), Instant.now()); // "3 hours ago"
 * RelativeTimeLite.format(deadline, Instant.now());            // "in 2 days"
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class RelativeTimeLite {
    private RelativeTimeLite() {}

    /**
     * Describes {@code target} relative to {@code now}, using one coarse unit.
     *
     * <p>The unit is chosen from the size of the gap: under a minute uses seconds,
     * under an hour uses minutes, under a day uses hours, and anything longer uses
     * days (so a year is {@code 365 days}). The amount is always rounded down, so
     * 119 seconds is {@code 1 minute}. Singular and plural forms are chosen
     * automatically. A target before {@code now} ends in {@code ago}; a target
     * equal to or after {@code now} starts with {@code in}, so identical instants
     * give {@code in 0 seconds}.
     *
     * <p>Output is English only and is not localized.
     *
     * @param target instant to describe
     * @param now reference instant, usually {@code Instant.now()}
     * @return text such as {@code 5 minutes ago} or {@code in 2 days}
     * @throws IllegalArgumentException if target or now is null
     */
    public static String format(Instant target, Instant now) {
        if (target == null || now == null) throw new IllegalArgumentException("target and now are required");
        Duration gap = Duration.between(now, target);
        long seconds = gap.abs().getSeconds();
        String unit;
        long amount;
        if (seconds < 60) { unit = "second"; amount = seconds; }
        else if (seconds < 3_600) { unit = "minute"; amount = seconds / 60; }
        else if (seconds < 86_400) { unit = "hour"; amount = seconds / 3_600; }
        else { unit = "day"; amount = seconds / 86_400; }
        String phrase = amount + " " + unit + (amount == 1 ? "" : "s");
        return gap.isNegative() ? phrase + " ago" : "in " + phrase;
    }
}
