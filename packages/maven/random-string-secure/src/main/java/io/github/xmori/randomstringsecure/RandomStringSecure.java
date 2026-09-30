package io.github.xmori.randomstringsecure;

import java.security.SecureRandom;

/**
 * Generates cryptographically random strings from an alphabet you choose, for
 * tokens, temporary passwords, and test data.
 *
 * <pre>{@code
 * String token = RandomStringSecure.generate(32, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
 * String hex = RandomStringSecure.generate(16, "0123456789abcdef");
 * }</pre>
 *
 * <p>This class is thread-safe; the shared {@link SecureRandom} is safe for
 * concurrent use.
 */
public final class RandomStringSecure {
    private RandomStringSecure() {}

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a random string whose characters are drawn from {@code alphabet}.
     *
     * <p>The alphabet is read as Unicode code points, so emoji and other
     * supplementary characters work, and duplicates are removed first so every
     * distinct character has the same probability. Each position is chosen
     * independently with {@link SecureRandom#nextInt(int)}, which has no modulo
     * bias. Each character adds {@code log2(alphabet size)} bits of randomness; for
     * a 62-character alphanumeric alphabet, 22 characters give about 131 bits.
     *
     * @param length number of code points in the result, from 1 through 1024
     * @param alphabet characters to choose from; must contain at least two distinct code points
     * @return a random string of {@code length} code points
     * @throws IllegalArgumentException if length is outside 1 through 1024, or alphabet is
     *     null or has fewer than two distinct code points
     */
    public static String generate(int length, String alphabet) {
        if (length < 1 || length > 1024 || alphabet == null) throw new IllegalArgumentException("invalid length or alphabet");
        int[] points = alphabet.codePoints().distinct().toArray();
        if (points.length < 2) throw new IllegalArgumentException("alphabet needs two distinct characters");
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) result.appendCodePoint(points[RANDOM.nextInt(points.length)]);
        return result.toString();
    }
}
