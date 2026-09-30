package io.github.xmori.initialsfromname;

import java.util.Locale;

/**
 * Builds uppercase initials from a person's name, for avatars and short labels.
 *
 * <pre>{@code
 * InitialsFromName.of("Ada Lovelace");        // "AL"
 * InitialsFromName.of("  jean-luc   picard "); // "JP"
 * InitialsFromName.of("émile zola");      // "ÉZ"
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class InitialsFromName {
    private InitialsFromName() {}

    /**
     * Returns the uppercase first letter of each whitespace-separated word.
     *
     * <p>Words are split on any Unicode whitespace, including tabs, newlines, and
     * the no-break space, and repeated whitespace is ignored. Each word
     * contributes its first Unicode code point, so letters outside the Basic
     * Multilingual Plane stay intact. Hyphenated parts are not split. The result
     * is uppercased with {@link Locale#ROOT}. A blank name returns an empty string.
     *
     * <p>Every word contributes, so shorten the result yourself if you only want,
     * for example, the first and last initials.
     *
     * @param name the name to read
     * @return the initials, possibly empty
     * @throws IllegalArgumentException if name is null
     */
    public static String of(String name) {
        if (name == null) throw new IllegalArgumentException("name is required");
        StringBuilder result = new StringBuilder();
        boolean atWordStart = true;
        for (int i = 0; i < name.length(); ) {
            int codePoint = name.codePointAt(i);
            if (isSeparator(codePoint)) {
                atWordStart = true;
            } else if (atWordStart) {
                result.appendCodePoint(codePoint);
                atWordStart = false;
            }
            i += Character.charCount(codePoint);
        }
        return result.toString().toUpperCase(Locale.ROOT);
    }

    private static boolean isSeparator(int codePoint) {
        return Character.isWhitespace(codePoint) || Character.isSpaceChar(codePoint);
    }
}
