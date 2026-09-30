# random-string-secure

Generate cryptographically random strings from an alphabet you choose — for API tokens, temporary passwords, and nonces.

```java
import io.github.xmori.randomstringsecure.RandomStringSecure;

String token = RandomStringSecure.generate(32, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
```

## Install

Maven coordinates: `io.github.xmori:random-string-secure:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>random-string-secure</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:random-string-secure:1.1.0")`.

## Usage

```java
String hex = RandomStringSecure.generate(32, "0123456789abcdef");                  // 128 bits
String pin = RandomStringSecure.generate(6, "0123456789");                         // about 20 bits
String friendly = RandomStringSecure.generate(10, "abcdefghjkmnpqrstuvwxyz23456789"); // no look-alikes
```

To reach a target strength, use length ≥ bits ÷ log₂(alphabet size). For 128 bits: 32 hex characters, or 22 characters from a 62-character alphanumeric alphabet.

## API

### `static String RandomStringSecure.generate(int length, String alphabet)`

| Parameter | Description |
| --- | --- |
| `length` | Number of characters in the result, 1 through 1024. |
| `alphabet` | Characters to choose from. Needs at least two distinct characters. |

Returns a string of `length` characters, each chosen independently and uniformly from the alphabet with `SecureRandom.nextInt`, which has no modulo bias.

- The alphabet is read as Unicode code points, so emoji and other characters outside the Basic Multilingual Plane work, and "length" means code points.
- Duplicate characters in the alphabet are removed first, so `"aab"` behaves like `"ab"` and every distinct character has the same probability.

Throws `IllegalArgumentException` when `length` is outside 1 through 1024, or `alphabet` is `null` or has fewer than two distinct characters.

The class is thread-safe.

## License

MIT
