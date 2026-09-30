package io.github.xmori.urlnormalizerlite;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

/**
 * Normalizes HTTP and HTTPS URLs so equivalent spellings compare equal, for
 * deduplication, cache keys, and allow lists.
 *
 * <pre>{@code
 * UrlNormalizerLite.normalize("HTTPS://Example.COM:443/a/./b/../c?q=1"); // "https://example.com/a/c?q=1"
 * UrlNormalizerLite.normalize("http://[::1]:8080");                      // "http://[::1]:8080/"
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class UrlNormalizerLite {
    private UrlNormalizerLite() {}

    /**
     * Normalizes an absolute HTTP or HTTPS URL without changing its percent escapes.
     *
     * <p>The following changes are made, and nothing else:
     * <ul>
     *   <li>the scheme and host are lowercased;</li>
     *   <li>{@code .} and {@code ..} path segments are resolved;</li>
     *   <li>the default port ({@code 80} for HTTP, {@code 443} for HTTPS) is removed;</li>
     *   <li>an empty path becomes {@code /}.</li>
     * </ul>
     *
     * <p>The query string and fragment are kept exactly as given, including
     * parameter order, and percent escapes are neither decoded nor re-cased.
     * IPv6 hosts keep their brackets. URLs with user information
     * ({@code user:pass@host}) are rejected so credentials are not copied into
     * normalized output.
     *
     * @param input URL to normalize
     * @return the normalized URL
     * @throws IllegalArgumentException if input is null or malformed, the scheme is not
     *     HTTP or HTTPS, there is no host, or the URL contains user information
     */
    public static String normalize(String input) {
        if (input == null) throw new IllegalArgumentException("URL is required");
        URI uri;
        try {
            uri = new URI(input).normalize();
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("invalid URL", exception);
        }
        String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.ROOT);
        if (!(scheme.equals("http") || scheme.equals("https")) || uri.getHost() == null || uri.getRawUserInfo() != null) throw new IllegalArgumentException("expected an HTTP URL without credentials");
        // URI.getHost() already includes the brackets around an IPv6 literal.
        String host = uri.getHost().toLowerCase(Locale.ROOT);
        int port = uri.getPort();
        if ((scheme.equals("http") && port == 80) || (scheme.equals("https") && port == 443)) port = -1;
        String path = uri.getRawPath().isEmpty() ? "/" : uri.getRawPath();
        StringBuilder result = new StringBuilder(input.length() + 1).append(scheme).append("://").append(host);
        if (port >= 0) result.append(':').append(port);
        result.append(path);
        if (uri.getRawQuery() != null) result.append('?').append(uri.getRawQuery());
        if (uri.getRawFragment() != null) result.append('#').append(uri.getRawFragment());
        return result.toString();
    }
}
