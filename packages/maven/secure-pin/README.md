# secure-pin

Generate cryptographically random numeric PINs and verification codes, such as the 6-digit codes sent by email or SMS.

```java
import io.github.xmori.securepin.SecurePin;

String code = SecurePin.generate(6); // for example "048213"
```

## Install

Maven coordinates: `io.github.xmori:secure-pin:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>secure-pin</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:secure-pin:1.1.0")`.

## Using verification codes safely

A 6-digit code has one million possible values, so it is only safe with limits around it:

- **Expire it** after a few minutes.
- **Limit attempts**, for example 5 per code, then require a new code.
- **Compare in constant time**: `MessageDigest.isEqual(expected.getBytes(US_ASCII), submitted.getBytes(US_ASCII))`.
- **Store a hash**, not the code, if it must be kept in a database.
- **Use it once**: delete it after a successful check.

## API

### `static String SecurePin.generate(int digits)`

Returns a string of exactly `digits` decimal digits. Each digit is chosen independently and uniformly with `SecureRandom`. Leading zeros are kept, so treat the result as a string, not a number.

Throws `IllegalArgumentException` when `digits` is outside 1 through 1024.

The class is thread-safe.

## Changes in 1.1.0

- The PIN is written into a `char` array instead of a `StringBuilder`. Output is unchanged.

## License

MIT
