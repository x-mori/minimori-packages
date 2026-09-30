# human-duration

Format a duration in milliseconds as compact text such as `1d 2h 5m` or `45s`, for logs, CLIs, and dashboards.

```java
import io.github.xmori.humanduration.HumanDuration;

HumanDuration.format(3_720_000); // "1h 2m"
```

## Install

Maven coordinates: `io.github.xmori:human-duration:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>human-duration</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:human-duration:1.1.0")`.

## Usage

```java
long started = System.nanoTime();
runJob();
log.info("Job finished in {}", HumanDuration.format(Duration.ofNanos(System.nanoTime() - started).toMillis()));
```

## API

### `static String HumanDuration.format(long milliseconds)`

| Input (ms) | Output |
| --- | --- |
| `0` or `999` | `0s` |
| `90_000` | `1m 30s` |
| `3_605_000` | `1h 5s` |
| `90_061_000` | `1d 1h 1m 1s` |
| `400 × 86_400_000` | `400d` |

- Units are days (`d`), hours (`h`), minutes (`m`), and seconds (`s`), separated by single spaces.
- Units with a value of zero are left out.
- Days are the largest unit; there are no weeks, months, or years.
- Milliseconds are truncated, not rounded.

Throws `IllegalArgumentException` when `milliseconds` is negative.

The class is stateless and thread-safe.

## License

MIT
