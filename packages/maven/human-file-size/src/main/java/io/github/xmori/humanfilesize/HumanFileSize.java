package io.github.xmori.humanfilesize;

import java.util.Locale;

/**
 * Formats byte counts as readable sizes such as {@code 1.5 MB} or {@code 1.5 MiB}.
 *
 * <pre>{@code
 * HumanFileSize.format(1_500, false);          // "1.5 KB"
 * HumanFileSize.format(1_572_864, true);       // "1.5 MiB"
 * HumanFileSize.format(Files.size(path), true);
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class HumanFileSize {
    private HumanFileSize() {}

    private static final String[] SI_UNITS = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
    private static final String[] BINARY_UNITS = {"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};

    /**
     * Formats a nonnegative byte count with SI or binary (IEC) units.
     *
     * <p>SI units use powers of 1000 (KB, MB, GB, TB, PB, EB). Binary units use
     * powers of 1024 (KiB, MiB, GiB, TiB, PiB, EiB), which is how most operating
     * systems report file sizes. Values below one unit step are shown as whole
     * bytes ({@code 999 B}); larger values have one decimal place, rounded half up,
     * with a dot as the decimal separator in every locale. A value that would
     * round up to the next step moves to the larger unit, so 999,950 bytes is
     * {@code 1.0 MB}, never {@code 1000.0 KB}.
     *
     * @param bytes the byte count, zero or greater
     * @param binary {@code true} for powers of 1024 and IEC units, {@code false} for powers of 1000 and SI units
     * @return the formatted size, for example {@code 1.5 KB}
     * @throws IllegalArgumentException if bytes is negative
     */
    public static String format(long bytes, boolean binary) {
        if (bytes < 0) throw new IllegalArgumentException("bytes must be nonnegative");
        int base = binary ? 1024 : 1000;
        if (bytes < base) return bytes + " B";
        String[] units = binary ? BINARY_UNITS : SI_UNITS;
        double value = bytes;
        int unit = 0;
        // Compare against base - 0.05 so a value that rounds to "1000.0" moves up a unit.
        while (unit < units.length - 1 && value >= base - 0.05) {
            value /= base;
            unit++;
        }
        return String.format(Locale.ROOT, "%.1f %s", value, units[unit]);
    }
}
