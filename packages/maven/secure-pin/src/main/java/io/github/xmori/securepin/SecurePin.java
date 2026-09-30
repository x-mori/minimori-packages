package io.github.xmori.securepin;

import java.security.SecureRandom;

/**
 * Generates cryptographically random numeric PINs and verification codes, such
 * as the six-digit codes sent by email or SMS.
 *
 * <pre>{@code
 * String code = SecurePin.generate(6); // for example "048213"
 * }</pre>
 *
 * <p>This class is thread-safe; the shared {@link SecureRandom} is safe for
 * concurrent use.
 */
public final class SecurePin {
    private SecurePin() {}

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a random PIN with exactly {@code digits} decimal digits.
     *
     * <p>Every digit is chosen independently and uniformly with
     * {@link SecureRandom}, and leading zeros are kept, so the result is a string
     * rather than a number. A 6-digit PIN has one million possible values: pair it
     * with an expiry time and a limit on failed attempts, compare submitted codes
     * with {@link java.security.MessageDigest#isEqual}, and store a hash rather
     * than the PIN when it must be kept.
     *
     * @param digits PIN length, from 1 through 1024
     * @return a string of decimal digits
     * @throws IllegalArgumentException if digits is outside 1 through 1024
     */
    public static String generate(int digits) {
        if (digits < 1 || digits > 1024) throw new IllegalArgumentException("digits must be 1..1024");
        char[] pin = new char[digits];
        for (int i = 0; i < digits; i++) pin[i] = (char) ('0' + RANDOM.nextInt(10));
        return new String(pin);
    }
}
