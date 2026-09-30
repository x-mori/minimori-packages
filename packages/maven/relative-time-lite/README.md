# relative-time-lite

Describe a moment relative to now in short English text such as `5 minutes ago` or `in 2 days` — for comment timestamps, activity feeds, and deadlines.

```java
import io.github.xmori.relativetimelite.RelativeTimeLite;

RelativeTimeLite.format(comment.createdAt(), Instant.now()); // "3 hours ago"
```

## Install

Maven coordinates: `io.github.xmori:relative-time-lite:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>relative-time-lite</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:relative-time-lite:1.1.0")`.

## API

### `static String RelativeTimeLite.format(Instant target, Instant now)`

Describes `target` as seen from `now`, using one unit:

| Gap | Unit | Example |
| --- | --- | --- |
| under 1 minute | seconds | `45 seconds ago` |
| under 1 hour | minutes | `in 5 minutes` |
| under 1 day | hours | `1 hour ago` |
| 1 day or more | days | `in 400 days` |

- Amounts are rounded **down**: 119 seconds is `1 minute`, and 500 ms is `0 seconds`.
- Singular and plural are chosen automatically (`1 day`, `2 days`).
- A `target` before `now` ends with `ago`. A `target` equal to or after `now` starts with `in`, so equal instants give `in 0 seconds`.
- Output is English only. Days are the largest unit; there are no weeks, months, or years.

Throws `IllegalArgumentException` when `target` or `now` is `null`.

Passing `now` explicitly keeps tests deterministic. For "just now" style wording, check the gap yourself before calling:

```java
String label = Duration.between(at, now).abs().getSeconds() < 10 ? "just now" : RelativeTimeLite.format(at, now);
```

The class is stateless and thread-safe.

## Changes in 1.1.0

- `null` arguments throw `IllegalArgumentException` instead of `NullPointerException`.
- Sub-second past gaps round toward zero: 500 ms in the past is `0 seconds ago` rather than `1 second ago`.

## License

MIT
