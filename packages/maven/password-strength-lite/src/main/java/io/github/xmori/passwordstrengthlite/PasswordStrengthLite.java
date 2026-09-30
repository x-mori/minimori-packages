package io.github.xmori.passwordstrengthlite;

import java.util.ArrayList;
import java.util.List;

/**
 * Gives simple, explainable feedback on password length and character variety.
 *
 * <pre>{@code
 * PasswordStrengthLite.Result result = PasswordStrengthLite.analyze("correct horse");
 * result.score();  // 2
 * result.advice(); // [A longer passphrase is stronger, Mix letter case, Add a number]
 * }</pre>
 *
 * <p>This is a lightweight hint for sign-up forms, not a security control. It does
 * not estimate entropy, detect dictionary words or keyboard patterns, or check
 * breach lists, so a weak password such as {@code Password123!} still scores well.
 * Always store passwords with a slow hash such as Argon2, scrypt, or bcrypt.
 *
 * <p>This class is stateless and thread-safe.
 */
public final class PasswordStrengthLite {
    private PasswordStrengthLite() {}

    /**
     * A password score and the advice for each check that did not pass.
     *
     * @param score number of passed checks, from 0 through 5
     * @param advice unmodifiable list of suggestions, in check order; empty when the score is 5
     */
    public record Result(int score, List<String> advice) {}

    /**
     * Scores a password with five checks, one point each.
     *
     * <ol>
     *   <li>At least 12 characters — otherwise "Use at least 12 characters".</li>
     *   <li>At least 20 characters — otherwise "A longer passphrase is stronger".</li>
     *   <li>Both an ASCII lowercase and an ASCII uppercase letter — otherwise "Mix letter case".</li>
     *   <li>An ASCII digit — otherwise "Add a number".</li>
     *   <li>A character that is not an ASCII letter or digit, such as punctuation, a space,
     *       or a non-Latin letter — otherwise "Add a symbol".</li>
     * </ol>
     *
     * <p>Length is counted in Unicode code points, so an emoji counts as one
     * character. The password is scanned once, and line breaks are treated like
     * any other character.
     *
     * @param password text to assess
     * @return the score and the advice for failed checks
     * @throws IllegalArgumentException if password is null
     */
    public static Result analyze(String password) {
        if (password == null) throw new IllegalArgumentException("password is required");
        boolean lower = false;
        boolean upper = false;
        boolean digit = false;
        boolean symbol = false;
        int length = 0;
        for (int i = 0; i < password.length(); length++) {
            int c = password.codePointAt(i);
            if (c >= 'a' && c <= 'z') lower = true;
            else if (c >= 'A' && c <= 'Z') upper = true;
            else if (c >= '0' && c <= '9') digit = true;
            else symbol = true;
            i += Character.charCount(c);
        }
        List<String> advice = new ArrayList<>(5);
        int score = 0;
        if (length >= 12) score++; else advice.add("Use at least 12 characters");
        if (length >= 20) score++; else advice.add("A longer passphrase is stronger");
        if (lower && upper) score++; else advice.add("Mix letter case");
        if (digit) score++; else advice.add("Add a number");
        if (symbol) score++; else advice.add("Add a symbol");
        return new Result(score, List.copyOf(advice));
    }
}
