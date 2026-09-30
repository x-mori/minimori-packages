package io.github.xmori.randomidlite;

import java.security.SecureRandom;

/**
 * Generates short, cryptographically random IDs that are easy to read aloud and
 * type, such as invite codes, order references, and support ticket numbers.
 *
 * <pre>{@code
 * String code = RandomIdLite.generate(8); // for example "K7XQ2M9D"
 * }</pre>
 *
 * <p>This class is thread-safe; the shared {@link SecureRandom} is safe for
 * concurrent use.
 */
public final class RandomIdLite {
    private RandomIdLite() {}

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final char[] ALPHABET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();

    /**
     * Generates a random ID from a 32-character alphabet without look-alike characters.
     *
     * <p>The alphabet is the digits 2 through 9 and the uppercase letters A through
     * Z without I and O, so 0/O and 1/I/L confusion cannot occur. Each character
     * carries 5 bits of randomness from {@link SecureRandom} and is chosen without
     * modulo bias, so {@code length} characters give {@code 5 * length} bits:
     * 8 characters give 40 bits and 16 give 80 bits.
     *
     * <p>IDs are random, not unique. Choose a length that makes collisions unlikely
     * for your volume and keep a unique constraint where duplicates would matter.
     *
     * @param length number of characters, from 1 through 1024
     * @return a random uppercase ID
     * @throws IllegalArgumentException if length is outside 1 through 1024
     */
    public static String generate(int length) {
        if (length < 1 || length > 1024) throw new IllegalArgumentException("length must be 1..1024");
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        char[] result = new char[length];
        // 256 is a multiple of 32, so masking the low 5 bits is unbiased.
        for (int i = 0; i < length; i++) result[i] = ALPHABET[bytes[i] & 31];
        return new String(result);
    }
}
