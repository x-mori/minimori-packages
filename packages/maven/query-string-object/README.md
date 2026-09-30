# query-string-object

Parse URL query strings into an ordered multimap, and build query strings from one, keeping every value of repeated keys.

```java
import io.github.xmori.querystringobject.QueryStringObject;

Map<String, List<String>> params = QueryStringObject.parse("?tag=java&tag=url&q=hello+world");
// {tag=[java, url], q=[hello world]}
```

## Install

Maven coordinates: `io.github.xmori:query-string-object:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

The package is published to GitHub Packages. Add the repository and dependency to `pom.xml`:

```xml
<repositories>
  <repository>
    <id>github-x-mori</id>
    <url>https://maven.pkg.github.com/x-mori/minimori-packages</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>io.github.xmori</groupId>
    <artifactId>query-string-object</artifactId>
    <version>1.1.0</version>
  </dependency>
</dependencies>
```

GitHub Packages requires authentication, even for public packages. Put a token with the `read:packages` scope in `~/.m2/settings.xml` under the same `id`:

```xml
<settings>
  <servers>
    <server>
      <id>github-x-mori</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>${env.GITHUB_TOKEN}</password>
    </server>
  </servers>
</settings>
```

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:query-string-object:1.1.0")`.

## Usage

Read the query of a `java.net.URI` (use the raw form so encoded `&` and `=` are not confused with separators):

```java
Map<String, List<String>> params = QueryStringObject.parse(Objects.requireNonNullElse(uri.getRawQuery(), ""));
String page = params.getOrDefault("page", List.of("1")).get(0);
```

Build a query string; use a `LinkedHashMap` to control the order:

```java
Map<String, List<String>> values = new LinkedHashMap<>();
values.put("q", List.of("a&b c"));
values.put("tag", List.of("x", "y"));
QueryStringObject.encode(values); // "q=a%26b+c&tag=x&tag=y"
```

## API

### `static Map<String, List<String>> QueryStringObject.parse(String query)`

- A leading `?` is optional. Pass only the query, not a full URL or a `#fragment`.
- Pairs are split on `&`, then at the first `=`, so `x=a=b` gives `x → [a=b]`.
- Keys and values are decoded as UTF-8 form data: `%XX` escapes are decoded and `+` becomes a space.
- A key without `=` gets the value `""`. Repeated keys keep every value, in order.
- Empty pairs (`a=1&&b=2`, or a trailing `&`) are skipped.

Returns a mutable `LinkedHashMap` in first-seen key order, with mutable value lists.

Throws `IllegalArgumentException` when `query` is `null` or contains an invalid `%` escape.

### `static String QueryStringObject.encode(Map<String, List<String>> values)`

Writes one `key=value` pair per list element, in the map's iteration order, joined with `&` and without a leading `?`. Keys and values use UTF-8 form encoding (`URLEncoder`), so spaces become `+`. A key with an empty list produces nothing.

Throws `IllegalArgumentException` when `values`, a key, a list, or a value is `null`.

`parse(encode(map))` returns an equal map when no list is empty.

The class is stateless and thread-safe.

## Changes in 1.1.0

- Empty pairs are skipped, matching browsers' `URLSearchParams`. In 1.0.1, `a=1&&b=2` also produced an entry with the key `""`.
- `null` arguments to `encode` throw `IllegalArgumentException` instead of `NullPointerException`.
- Parsing and encoding no longer create intermediate arrays and lists.

## License

MIT
