# human-file-size

Format byte counts as readable sizes, using SI units (`1.5 MB`, powers of 1000) or binary units (`1.5 MiB`, powers of 1024).

```java
import io.github.xmori.humanfilesize.HumanFileSize;

HumanFileSize.format(1_500, false);    // "1.5 KB"
HumanFileSize.format(1_572_864, true); // "1.5 MiB"
```

## Install

Maven coordinates: `io.github.xmori:human-file-size:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>human-file-size</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:human-file-size:1.1.0")`.

## Usage

```java
String size = HumanFileSize.format(Files.size(path), true); // matches what most file managers show
```

## API

### `static String HumanFileSize.format(long bytes, boolean binary)`

| `binary` | Base | Units |
| --- | --- | --- |
| `false` | 1000 | B, KB, MB, GB, TB, PB, EB |
| `true` | 1024 | B, KiB, MiB, GiB, TiB, PiB, EiB |

- Sizes below one step are whole bytes: `999 B`, or `1000 B` in binary mode.
- Larger sizes have one decimal place, rounded half up, with a dot as the decimal separator in every locale.
- A value that would round to `1000.0` of one unit is shown as `1.0` of the next: 999,950 bytes is `1.0 MB`.
- `Long.MAX_VALUE` is `9.2 EB` or `8.0 EiB`.

Throws `IllegalArgumentException` when `bytes` is negative.

The class is stateless and thread-safe.

## Changes in 1.1.0

- Rounding no longer produces `1000.0 KB` (or `1024.0 KiB`); such values move up to the next unit.
- Exabytes (`EB`, `EiB`) were added, so very large values no longer show as thousands of petabytes.
- The unit tables are allocated once instead of on every call.

## License

MIT
