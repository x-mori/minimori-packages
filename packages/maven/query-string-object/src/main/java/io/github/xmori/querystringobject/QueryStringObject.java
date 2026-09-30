package io.github.xmori.querystringobject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Parses and builds URL query strings, keeping every value of repeated keys.
 *
 * <pre>{@code
 * Map<String, List<String>> params = QueryStringObject.parse("?tag=java&tag=url&q=hello+world");
 * // {tag=[java, url], q=[hello world]}
 *
 * QueryStringObject.encode(Map.of("q", List.of("a&b"))); // "q=a%26b"
 * }</pre>
 *
 * <p>This class is stateless and thread-safe.
 */
public final class QueryStringObject {
    private QueryStringObject() {}

    /**
     * Parses a query string into an insertion-ordered multimap.
     *
     * <p>Pairs are separated by {@code &} and split at the first {@code =}. Keys
     * and values are decoded as UTF-8 form data, so {@code +} becomes a space. A
     * key without {@code =} gets an empty-string value, and repeated keys keep
     * every value in order. Empty pairs such as the gap in {@code a=1&&b=2} are
     * skipped, matching browsers' {@code URLSearchParams}. A leading {@code ?} is
     * optional. Do not pass a full URL or a fragment.
     *
     * @param query query text, with or without a leading {@code ?}
     * @return a mutable {@link LinkedHashMap} from each key to a mutable list of its values
     * @throws IllegalArgumentException if query is null or contains an invalid percent escape
     */
    public static Map<String, List<String>> parse(String query) {
        if (query == null) throw new IllegalArgumentException("query is required");
        Map<String, List<String>> result = new LinkedHashMap<>();
        int start = query.startsWith("?") ? 1 : 0;
        while (start <= query.length()) {
            int end = query.indexOf('&', start);
            if (end < 0) end = query.length();
            if (end > start) {
                int equals = query.indexOf('=', start);
                boolean hasValue = equals >= 0 && equals < end;
                String key = decode(query.substring(start, hasValue ? equals : end));
                String value = hasValue ? decode(query.substring(equals + 1, end)) : "";
                result.computeIfAbsent(key, ignored -> new ArrayList<>(1)).add(value);
            }
            start = end + 1;
        }
        return result;
    }

    /**
     * Encodes a multimap as a query string.
     *
     * <p>Each value becomes one {@code key=value} pair, in the map's iteration
     * order, so use a {@link LinkedHashMap} for a predictable order. Keys and
     * values use UTF-8 form encoding ({@link URLEncoder}), which writes spaces as
     * {@code +}. A key with an empty list produces no pairs. The result has no
     * leading {@code ?}.
     *
     * @param values map of keys to zero or more values
     * @return the encoded query string, possibly empty
     * @throws IllegalArgumentException if values, a key, a list, or a value is null
     */
    public static String encode(Map<String, List<String>> values) {
        if (values == null) throw new IllegalArgumentException("values are required");
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : values.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) throw new IllegalArgumentException("null key or value list");
            String key = URLEncoder.encode(entry.getKey(), UTF_8);
            for (String value : entry.getValue()) {
                if (value == null) throw new IllegalArgumentException("null value");
                if (result.length() > 0) result.append('&');
                result.append(key).append('=').append(URLEncoder.encode(value, UTF_8));
            }
        }
        return result.toString();
    }

    private static String decode(String text) {
        return URLDecoder.decode(text, UTF_8);
    }
}
