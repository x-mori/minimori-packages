# random-id-lite

Generate short, cryptographically random IDs that are easy to read aloud and type: invite codes, order references, and support ticket numbers.

```java
import io.github.xmori.randomidlite.RandomIdLite;

String code = RandomIdLite.generate(8); // for example "K7XQ2M9D"
```

## Install

Maven coordinates: `io.github.xmori:random-id-lite:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>random-id-lite</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:random-id-lite:1.1.0")`.

## Choosing a length

Each character carries 5 bits of randomness (32 possible characters).

| Length | Bits | Possible IDs | Rough use |
| ---: | ---: | ---: | --- |
| 6 | 30 | about 1 billion | short-lived codes checked together with an account |
| 8 | 40 | about 1.1 trillion | order and ticket references |
| 12 | 60 | about 10<sup>18</sup> | public IDs that should not be guessable |
| 26 | 130 | about 10<sup>39</sup> | secrets such as tokens |

IDs are random, **not** guaranteed unique. With *n* IDs and *b* bits, the chance of any duplicate is about *n*² / 2<sup>b+1</sup>, so keep a unique constraint in your database and retry on conflict.

## API

### `static String RandomIdLite.generate(int length)`

Returns `length` characters drawn from `23456789ABCDEFGHJKLMNPQRSTUVWXYZ`: the digits 2–9 and the uppercase letters without `I` and `O`. Removing `0`, `1`, `I`, and `O` prevents look-alike mistakes when people read or type IDs.

Characters come from `SecureRandom` with no modulo bias, so every character is equally likely.

Throws `IllegalArgumentException` when `length` is outside 1 through 1024.

The class is thread-safe.

## Changes in 1.1.0

- Random bytes are drawn in one `SecureRandom` call per ID instead of one call per character. The alphabet and distribution are unchanged.

## License

MIT
