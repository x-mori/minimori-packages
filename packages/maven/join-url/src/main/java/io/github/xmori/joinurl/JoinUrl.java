package io.github.xmori.joinurl;

import java.net.URI;

/**
 * Appends path segments to a base URL without doubled or missing slashes.
 *
 * <pre>{@code
 * JoinUrl.join("https://api.example.com/v1/", "/users/", "42"); // "https://api.example.com/v1/users/42"
 * JoinUrl.join("https://example.com?lang=en", "docs");         // "https://example.com/docs?lang=en"
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class JoinUrl {
    private JoinUrl() {}

    /**
     * Appends path segments to an absolute base URL.
     *
     * <p>Leading and trailing slashes on each segment are removed and exactly one
     * slash is placed between parts. Empty segments, and segments made only of
     * slashes, are skipped. The base URL's query string and fragment are kept at
     * the end of the result. A trailing slash on the base path is not preserved
     * after a segment is appended.
     *
     * <p>Segments are inserted as raw text and are not percent-encoded. Encode
     * user input first, for example with
     * {@code URLEncoder.encode(value, UTF_8).replace("+", "%20")}. A slash inside a
     * segment (such as {@code "a/b"}) is kept and creates two path levels.
     *
     * @param base absolute URL with a scheme and host and without user information
     * @param segments path segments to append, in order
     * @return the joined URL
     * @throws IllegalArgumentException if base is blank, relative, contains credentials,
     *     or is not a valid URI; if a segment is null; or if the result is not a valid URI
     */
    public static String join(String base, String... segments) {
        if (base == null || base.isBlank()) throw new IllegalArgumentException("base URL is required");
        if (segments == null) throw new IllegalArgumentException("null segments");
        URI uri = URI.create(base);
        if (uri.getScheme() == null || uri.getHost() == null || uri.getRawUserInfo() != null) throw new IllegalArgumentException("absolute base URL without credentials is required");
        String rawPath = uri.getRawPath();
        StringBuilder path = new StringBuilder(rawPath == null ? "" : rawPath);
        for (String segment : segments) {
            if (segment == null) throw new IllegalArgumentException("null segment");
            int start = 0;
            int end = segment.length();
            while (start < end && segment.charAt(start) == '/') start++;
            while (end > start && segment.charAt(end - 1) == '/') end--;
            if (start == end) continue;
            if (path.length() == 0 || path.charAt(path.length() - 1) != '/') path.append('/');
            path.append(segment, start, end);
        }
        StringBuilder result = new StringBuilder(base.length() + path.length())
                .append(uri.getScheme()).append("://").append(uri.getRawAuthority()).append(path);
        if (uri.getRawQuery() != null) result.append('?').append(uri.getRawQuery());
        if (uri.getRawFragment() != null) result.append('#').append(uri.getRawFragment());
        String joined = result.toString();
        URI.create(joined); // reject segments that make the URL invalid
        return joined;
    }
}
