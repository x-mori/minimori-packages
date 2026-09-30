package io.github.xmori.otpgeneratorlite;

import java.security.GeneralSecurityException;
import java.time.Instant;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Generates RFC 6238 time-based one-time passwords (TOTP) with HMAC-SHA1, the
 * algorithm used by Google Authenticator, Microsoft Authenticator, and most other
 * authenticator apps.
 *
 * <pre>{@code
 * byte[] secret = ...; // raw shared secret bytes (decode the Base32 text from the QR code first)
 * String code = OtpGeneratorLite.generate(secret, Instant.now(), 6, 30);
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class OtpGeneratorLite {
    private OtpGeneratorLite() {}

    private static final int[] POWERS_OF_TEN = {1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000, 1_000_000_000};

    /**
     * Generates a time-based one-time password for the given instant.
     *
     * <p>The moving counter is {@code floor(epochSeconds / stepSeconds)}, encoded
     * as 8 big-endian bytes and signed with HMAC-SHA1. The code is taken from the
     * signature using RFC 4226 dynamic truncation. The same secret, instant step,
     * and digit count always give the same code, and codes are zero-padded to
     * exactly {@code digits} characters.
     *
     * <p>To verify a code submitted by a user, generate codes for the current step
     * and usually one step either side to allow for clock drift, compare them with
     * {@link java.security.MessageDigest#isEqual} to avoid timing leaks, and reject
     * a code that has already been used. Keep the secret in secure storage.
     *
     * @param secret raw shared secret bytes; RFC 4226 recommends at least 20 bytes
     * @param time instant for the code, usually {@code Instant.now()}
     * @param digits number of digits, from 6 through 9; authenticator apps normally use 6
     * @param stepSeconds positive time-step length in seconds; authenticator apps normally use 30
     * @return the zero-padded numeric code
     * @throws IllegalArgumentException if the secret is null or empty, time is null,
     *     digits is outside 6 through 9, or stepSeconds is not positive
     * @throws IllegalStateException if the JVM does not provide HmacSHA1
     */
    public static String generate(byte[] secret, Instant time, int digits, int stepSeconds) {
        if (secret == null || secret.length == 0 || time == null || digits < 6 || digits > 9 || stepSeconds < 1) throw new IllegalArgumentException("invalid OTP settings");
        long counter = Math.floorDiv(time.getEpochSecond(), stepSeconds);
        byte[] message = new byte[8];
        for (int i = 7; i >= 0; i--, counter >>>= 8) message[i] = (byte) counter;
        byte[] hash;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(secret, "HmacSHA1"));
            hash = mac.doFinal(message);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("HmacSHA1 is unavailable", exception);
        }
        int offset = hash[hash.length - 1] & 0x0f;
        int number = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
        String code = Integer.toString(number % POWERS_OF_TEN[digits]);
        return "0".repeat(digits - code.length()) + code;
    }
}
