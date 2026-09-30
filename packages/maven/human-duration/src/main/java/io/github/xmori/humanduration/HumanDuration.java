package io.github.xmori.humanduration;

/**
 * Formats millisecond durations as compact text such as {@code 1d 2h 5m}.
 *
 * <pre>{@code
 * HumanDuration.format(3_720_000);         // "1h 2m"
 * HumanDuration.format(Duration.ofSeconds(90).toMillis()); // "1m 30s"
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class HumanDuration {
    private HumanDuration() {}

    /**
     * Formats a duration in milliseconds using days, hours, minutes, and seconds.
     *
     * <p>Units are separated by single spaces, and any unit whose value is zero is
     * left out ({@code 1h 5s}, not {@code 1h 0m 5s}). Days are the largest unit, so
     * long durations appear as {@code 400d 3h}. Milliseconds are truncated, not
     * rounded, and a duration shorter than one second formats as {@code 0s}.
     *
     * @param milliseconds nonnegative duration in milliseconds
     * @return compact English text such as {@code 1h 2m}
     * @throws IllegalArgumentException if milliseconds is negative
     */
    public static String format(long milliseconds) {
        if (milliseconds < 0) throw new IllegalArgumentException("duration must be nonnegative");
        long seconds = milliseconds / 1000;
        long days = seconds / 86_400;
        long hours = seconds / 3_600 % 24;
        long minutes = seconds / 60 % 60;
        seconds %= 60;
        StringBuilder result = new StringBuilder(24);
        if (days > 0) append(result, days, 'd');
        if (hours > 0) append(result, hours, 'h');
        if (minutes > 0) append(result, minutes, 'm');
        if (seconds > 0 || result.length() == 0) append(result, seconds, 's');
        return result.toString();
    }

    private static void append(StringBuilder result, long amount, char unit) {
        if (result.length() > 0) result.append(' ');
        result.append(amount).append(unit);
    }
}
