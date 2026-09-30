# url-normalizer-lite

Normalize HTTP and HTTPS URLs so equivalent spellings compare equal — for deduplication, cache keys, and allow lists — without touching their percent escapes or query strings.

```java
import io.github.xmori.urlnormalizerlite.UrlNormalizerLite;

UrlNormalizerLite.normalize("HTTPS://Example.COM:443/a/./b/../c?q=1"); // "https://example.com/a/c?q=1"
```

## Install

Maven coordinates: `io.github.xmori:url-normalizer-lite:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>url-normalizer-lite</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:url-normalizer-lite:1.1.0")`.

## API

### `static String UrlNormalizerLite.normalize(String input)`

Makes exactly these changes:

| Change | Before | After |
| --- | --- | --- |
| Lowercase scheme and host | `HTTPS://Example.COM/` | `https://example.com/` |
| Resolve `.` and `..` segments | `/a/./b/../c` | `/a/c` |
| Remove default port | `http://host:80/`, `https://host:443/` | `http://host/`, `https://host/` |
| Empty path becomes `/` | `https://example.com` | `https://example.com/` |

Everything else is kept exactly: path case, percent escapes (`%2f` stays lowercase), query parameter order, and the fragment. IPv6 hosts keep their brackets: `http://[::1]:8080` becomes `http://[::1]:8080/`.

Throws `IllegalArgumentException` when `input`:

- is `null` or not a valid URI;
- has a scheme other than `http` or `https`, or no host;
- contains user information such as `user:pass@`, which is rejected so credentials are never copied into normalized output.

The class is stateless and thread-safe.

## Changes in 1.1.0

- IPv6 hosts are no longer wrapped in a second pair of brackets. In 1.0.1, `http://[::1]/` became the invalid `http://[[::1]]/`.
- `null` input throws `IllegalArgumentException` instead of `NullPointerException`.

## License

MIT
