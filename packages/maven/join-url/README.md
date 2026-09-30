# join-url

Append path segments to a base URL with exactly one slash between parts, keeping the base URL's query string and fragment.

```java
import io.github.xmori.joinurl.JoinUrl;

JoinUrl.join("https://api.example.com/v1/", "/users/", "42"); // "https://api.example.com/v1/users/42"
```

## Install

Maven coordinates: `io.github.xmori:join-url:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>join-url</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:join-url:1.1.0")`.

## Usage

```java
JoinUrl.join("https://example.com?lang=en#top", "docs", "intro"); // "https://example.com/docs/intro?lang=en#top"
```

Segments are inserted as-is. Percent-encode user input first, or a space or `?` will make the URL invalid or change its meaning:

```java
String name = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
JoinUrl.join(baseUrl, "files", name);
```

## API

### `static String JoinUrl.join(String base, String... segments)`

| Parameter | Description |
| --- | --- |
| `base` | Absolute URL with a scheme and host, such as `https://example.com/api`. User information (`user:pass@`) is not allowed. |
| `segments` | Path parts to append, in order. |

Behavior:

- Leading and trailing slashes on each segment are removed, and one slash is placed between parts.
- Empty segments and segments made only of slashes are skipped.
- A slash inside a segment is kept, so `"a/b"` adds two path levels.
- The base URL's query string and fragment move to the end of the result.
- With no segments, the base URL is returned as given, including any trailing slash. No other normalization is done; use `url-normalizer-lite` for that.

Throws `IllegalArgumentException` when `base` is `null`, blank, relative, invalid, or contains user information; when `segments` or any segment is `null`; or when the joined result is not a valid URI (for example, a segment contains a space).

The class is stateless and thread-safe.

## Changes in 1.1.0

- Segments are trimmed without regular expressions, and the result is built in a single buffer.
- A `null` segments array now throws `IllegalArgumentException` instead of `NullPointerException`.

## License

MIT
