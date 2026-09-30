package io.github.xmori.maskstring;

/**
 * Masks all but the last few characters of a string, for card numbers, tokens,
 * and phone numbers shown in logs or UIs.
 *
 * <pre>{@code
 * MaskString.of("4111111111111111", 4, '*'); // "************1111"
 * MaskString.of("secret", 0, '•');      // "••••••"
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class MaskString {
    private MaskString() {}

    /**
     * Replaces every character except the last {@code visible} ones with {@code mask}.
     *
     * <p>Characters are counted as Unicode code points, so an emoji or other
     * supplementary character is masked or kept as one unit and never split. The
     * result has the same number of code points as the input, which reveals the
     * input length; pad or truncate first if the length is sensitive. When
     * {@code visible} is at least the input length, the input is returned
     * unchanged.
     *
     * @param value the text to mask
     * @param visible number of trailing code points to leave visible, zero or greater
     * @param mask replacement character for each hidden code point
     * @return the masked string
     * @throws IllegalArgumentException if value is null or visible is negative
     */
    public static String of(String value, int visible, char mask) {
        if (value == null || visible < 0) throw new IllegalArgumentException("invalid mask arguments");
        int length = value.codePointCount(0, value.length());
        int hidden = length - visible;
        if (hidden <= 0) return value;
        int visibleFrom = value.offsetByCodePoints(0, hidden);
        return String.valueOf(mask).repeat(hidden) + value.substring(visibleFrom);
    }
}
