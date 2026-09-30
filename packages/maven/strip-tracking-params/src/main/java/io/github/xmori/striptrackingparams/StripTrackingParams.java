package io.github.xmori.striptrackingparams;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Set;

/**
 * Removes common analytics and ad-click tracking parameters from URLs before
 * they are stored, compared, or shared.
 *
 * <pre>{@code
 * StripTrackingParams.strip("https://example.com/post?id=7&utm_source=news&fbclid=abc#top");
 * // "https://example.com/post?id=7#top"
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class StripTrackingParams {
    private StripTrackingParams() {}

    private static final Set<String> TRACKING_KEYS = Set.of("fbclid", "gclid", "msclkid");

    /**
     * Removes tracking parameters from an absolute URL.
     *
     * <p>A parameter is removed when its name starts with {@code utm_} or is
     * exactly {@code fbclid}, {@code gclid}, or {@code msclkid}, compared without
     * regard to case. Every other parameter is kept byte for byte, in its original
     * order and with its original encoding. The scheme, host, path, and fragment
     * are unchanged. If every parameter is removed, the {@code ?} is dropped too.
     * A URL without a query string is returned as given.
     *
     * @param input absolute URL with a scheme and host
     * @return the URL without tracking parameters
     * @throws IllegalArgumentException if input is null, malformed, or not an absolute URL with a host
     */
    public static String strip(String input) {
        if (input == null) throw new IllegalArgumentException("URL is required");
        URI uri;
        try {
            uri = new URI(input);
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("invalid URL", exception);
        }
        if (uri.getScheme() == null || uri.getHost() == null) throw new IllegalArgumentException("absolute URL required");
        String query = uri.getRawQuery();
        if (query == null) return input;
        StringBuilder kept = new StringBuilder(query.length());
        int start = 0;
        while (start <= query.length()) {
            int end = query.indexOf('&', start);
            if (end < 0) end = query.length();
            String pair = query.substring(start, end);
            if (!isTracking(pair)) {
                if (kept.length() > 0) kept.append('&');
                kept.append(pair);
            }
            start = end + 1;
        }
        // In a hierarchical URI the first '?' always starts the query.
        StringBuilder result = new StringBuilder(input.length()).append(input, 0, input.indexOf('?'));
        if (kept.length() > 0) result.append('?').append(kept);
        if (uri.getRawFragment() != null) result.append('#').append(uri.getRawFragment());
        return result.toString();
    }

    private static boolean isTracking(String pair) {
        int equals = pair.indexOf('=');
        String key = (equals < 0 ? pair : pair.substring(0, equals)).toLowerCase(Locale.ROOT);
        return key.startsWith("utm_") || TRACKING_KEYS.contains(key);
    }
}
