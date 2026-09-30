# strip-tracking-params

Remove analytics and ad-click tracking parameters (`utm_*`, `fbclid`, `gclid`, `msclkid`) from URLs before storing, comparing, or sharing them.

```java
import io.github.xmori.striptrackingparams.StripTrackingParams;

StripTrackingParams.strip("https://example.com/post?id=7&utm_source=news&fbclid=abc#top");
// "https://example.com/post?id=7#top"
```

## Install

Maven coordinates: `io.github.xmori:strip-tracking-params:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>strip-tracking-params</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:strip-tracking-params:1.1.0")`.

## Usage

Combine with [`url-normalizer-lite`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/url-normalizer-lite) to build a canonical key for deduplicating links:

```java
String key = UrlNormalizerLite.normalize(StripTrackingParams.strip(sharedUrl));
```

## API

### `static String StripTrackingParams.strip(String input)`

Removes a query parameter when its name, compared without regard to case:

- starts with `utm_` (such as `utm_source`, `utm_medium`, `UTM_Campaign`), or
- is exactly `fbclid`, `gclid`, or `msclkid`.

Everything else is left exactly as given: other parameters keep their order and encoding, and the scheme, host, path, and fragment are unchanged. If every parameter is removed, the `?` is removed too. A URL without a query string is returned as given.

Throws `IllegalArgumentException` when `input` is `null`, is not a valid URI, or is not absolute with a host.

The class is stateless and thread-safe.

## Changes in 1.1.0

- `null` input throws `IllegalArgumentException` instead of `NullPointerException`.
- The tracking-key set is created once instead of for every parameter, and the query is scanned without regular expressions.

## License

MIT
