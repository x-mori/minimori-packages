# otp-generator-lite

Generate RFC 6238 time-based one-time passwords (TOTP) — the 6-digit codes shown by Google Authenticator, Microsoft Authenticator, 1Password, and similar apps.

```java
import io.github.xmori.otpgeneratorlite.OtpGeneratorLite;

String code = OtpGeneratorLite.generate(secretBytes, Instant.now(), 6, 30); // for example "492039"
```

## Install

Maven coordinates: `io.github.xmori:otp-generator-lite:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>otp-generator-lite</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:otp-generator-lite:1.1.0")`.

## Usage

### The secret

Authenticator apps share the secret as Base32 text (the `secret=` value in an `otpauth://` QR code). This package takes the **raw bytes**, so decode the Base32 text first with your Base32 library, for example Apache Commons Codec's `new Base32().decode(text)`. To create a new secret, generate 20 random bytes with `SecureRandom` and Base32-encode them for the QR code.

### Verifying a submitted code

Allow for clock drift by accepting the neighbouring time steps, compare in constant time, and remember the last step used so a code cannot be replayed:

```java
static OptionalLong verify(byte[] secret, String submitted, long lastUsedStep) {
    Instant now = Instant.now();
    for (int drift = -1; drift <= 1; drift++) {
        Instant at = now.plusSeconds(drift * 30L);
        long step = Math.floorDiv(at.getEpochSecond(), 30);
        String expected = OtpGeneratorLite.generate(secret, at, 6, 30);
        if (step > lastUsedStep && MessageDigest.isEqual(expected.getBytes(US_ASCII), submitted.getBytes(US_ASCII))) {
            return OptionalLong.of(step); // store as the new lastUsedStep
        }
    }
    return OptionalLong.empty();
}
```

Also limit the number of failed attempts per account.

## API

### `static String OtpGeneratorLite.generate(byte[] secret, Instant time, int digits, int stepSeconds)`

| Parameter | Description |
| --- | --- |
| `secret` | Raw shared secret bytes. RFC 4226 recommends at least 20 bytes (160 bits). |
| `time` | Instant to generate the code for, usually `Instant.now()`. |
| `digits` | Code length, 6 through 9. Authenticator apps use 6. |
| `stepSeconds` | Time-step length in seconds, at least 1. Authenticator apps use 30. |

Returns the code as a string of exactly `digits` digits, zero-padded, so keep it as a string.

The algorithm is HMAC-SHA1 over the step counter `floor(epochSeconds / stepSeconds)`, with RFC 4226 dynamic truncation. Output matches the RFC 6238 SHA-1 test vectors. SHA-256 and SHA-512 variants are not supported.

Throws `IllegalArgumentException` when `secret` is `null` or empty, `time` is `null`, `digits` is outside 6 through 9, or `stepSeconds` is less than 1. Throws `IllegalStateException` if the JVM has no HmacSHA1 provider (standard JVMs always include one).

The class is stateless and thread-safe.

## Changes in 1.1.0

- The Maven description now says what the package does (it previously described a random numeric code generator).
- Codes are zero-padded without `String.format`, and the counter is encoded without a `ByteBuffer`. Output is unchanged.

## License

MIT
