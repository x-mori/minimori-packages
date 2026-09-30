package io.github.xmori.querystringobject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QueryStringObjectTest {
    @Test void parsesRepeatedKeysInOrder() {
        Map<String, List<String>> params = QueryStringObject.parse("?tag=java&tag=url&q=hello+world&empty&x=a=b");
        assertEquals(List.of("tag", "q", "empty", "x"), List.copyOf(params.keySet()));
        assertEquals(List.of("java", "url"), params.get("tag"));
        assertEquals(List.of("hello world"), params.get("q"));
        assertEquals(List.of(""), params.get("empty"));
        assertEquals(List.of("a=b"), params.get("x"));
    }

    @Test void decodesUtf8AndSkipsEmptyPairs() {
        assertEquals(Map.of("name", List.of("café")), QueryStringObject.parse("name=caf%C3%A9"));
        assertEquals(List.of("a", "b"), List.copyOf(QueryStringObject.parse("a=1&&b=2&").keySet()));
        assertTrue(QueryStringObject.parse("").isEmpty());
        assertTrue(QueryStringObject.parse("?").isEmpty());
    }

    @Test void encodesAndRoundTrips() {
        Map<String, List<String>> values = new LinkedHashMap<>();
        values.put("q", List.of("a&b c"));
        values.put("tag", List.of("x", "y"));
        values.put("none", List.of());
        assertEquals("q=a%26b+c&tag=x&tag=y", QueryStringObject.encode(values));
        values.remove("none");
        assertEquals(values, QueryStringObject.parse(QueryStringObject.encode(values)));
    }

    @Test void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> QueryStringObject.parse(null));
        assertThrows(IllegalArgumentException.class, () -> QueryStringObject.parse("a=%zz"));
        assertThrows(IllegalArgumentException.class, () -> QueryStringObject.encode(null));
    }
}
