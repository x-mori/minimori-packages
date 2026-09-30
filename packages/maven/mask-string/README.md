# mask-string

Hide all but the last few characters of a string — for card numbers, account numbers, phone numbers, and tokens shown in UIs or logs.

```java
import io.github.xmori.maskstring.MaskString;

MaskString.of("4111111111111111", 4, '*'); // "************1111"
```

## Install

Maven coordinates: `io.github.xmori:mask-string:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>mask-string</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:mask-string:1.1.0")`.

## Usage

```java
MaskString.of("sk_live_51H8abcdef", 4, '•'); // "••••••••••••••cdef"
MaskString.of("secret", 0, '#');             // "######"
MaskString.of("abc", 10, '*');               // "abc" (nothing to hide)
```

The output has the same length as the input, which reveals how long the secret is. If that matters, mask a fixed-length placeholder instead:

```java
String shown = "****" + token.substring(token.length() - 4);
```

## API

### `static String MaskString.of(String value, int visible, char mask)`

| Parameter | Description |
| --- | --- |
| `value` | Text to mask. |
| `visible` | Number of trailing characters to leave visible; `0` or more. |
| `mask` | Character that replaces each hidden character. |

Returns `value` with every character except the last `visible` replaced by `mask`. When `visible` is at least the length of `value`, `value` is returned unchanged.

Characters are counted as Unicode code points, so an emoji or other character outside the Basic Multilingual Plane is masked or kept as one unit and never split.

Throws `IllegalArgumentException` when `value` is `null` or `visible` is negative.

The class is stateless and thread-safe.

## Changes in 1.1.0

- Masking builds the result with one repeated mask run and one substring, instead of creating a string for every character. Results are unchanged.

## License

MIT
