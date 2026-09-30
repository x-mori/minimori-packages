package io.github.xmori.emailnormalizer;

import java.util.Locale;

/**
 * Normalizes email addresses for storage and comparison.
 *
 * <pre>{@code
 * EmailNormalizer.normalize("  Jane.Doe@Example.COM "); // "Jane.Doe@example.com"
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class EmailNormalizer {
    private EmailNormalizer() {}

    /**
     * Trims an email address and lowercases its domain.
     *
     * <p>The local part (before {@code @}) keeps its case because RFC 5321 allows
     * mail servers to treat it as case-sensitive. Provider-specific rules, such as
     * removing dots or {@code +tag} suffixes for Gmail, are not applied.
     *
     * <p>Validation is structural only: the address must contain exactly one
     * {@code @} with text on both sides and no whitespace after trimming. The
     * method does not check that the domain exists or that mail is deliverable.
     * Domains are lowercased with {@link Locale#ROOT}; internationalized domains
     * are not converted to Punycode.
     *
     * @param email address to normalize
     * @return the trimmed address with a lowercase domain
     * @throws IllegalArgumentException if email is null or not structurally valid
     */
    public static String normalize(String email) {
        if (email == null) throw new IllegalArgumentException("email is required");
        String value = email.strip();
        int at = value.indexOf('@');
        if (at < 1 || at == value.length() - 1 || value.indexOf('@', at + 1) >= 0 || containsWhitespace(value)) throw new IllegalArgumentException("invalid email");
        return value.substring(0, at + 1) + value.substring(at + 1).toLowerCase(Locale.ROOT);
    }

    private static boolean containsWhitespace(String value) {
        for (int i = 0; i < value.length(); ) {
            int codePoint = value.codePointAt(i);
            if (Character.isWhitespace(codePoint)) return true;
            i += Character.charCount(codePoint);
        }
        return false;
    }
}
