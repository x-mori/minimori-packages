# password-strength-lite

Give simple, explainable feedback on a password's length and character variety — a score from 0 to 5 plus a list of suggestions for sign-up forms.

```java
import io.github.xmori.passwordstrengthlite.PasswordStrengthLite;

PasswordStrengthLite.Result result = PasswordStrengthLite.analyze("correct horse");
result.score();  // 2
result.advice(); // [A longer passphrase is stronger, Mix letter case, Add a number]
```

> **This is a hint, not a security control.** It does not estimate entropy, detect dictionary words or keyboard patterns, or check breach lists, so a weak password such as `Password123!` still scores 4. Enforce a minimum length on the server, check passwords against a breach list, and store them with Argon2, scrypt, or bcrypt.

## Install

Maven coordinates: `io.github.xmori:password-strength-lite:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>password-strength-lite</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:password-strength-lite:1.1.0")`.

## API

### `static PasswordStrengthLite.Result PasswordStrengthLite.analyze(String password)`

Scores the password with five checks, one point each. Each failed check adds its advice, in this order:

| # | Check | Advice when it fails |
| --- | --- | --- |
| 1 | At least 12 characters | `Use at least 12 characters` |
| 2 | At least 20 characters | `A longer passphrase is stronger` |
| 3 | An ASCII lowercase **and** an ASCII uppercase letter | `Mix letter case` |
| 4 | An ASCII digit | `Add a number` |
| 5 | Any other character: punctuation, a space, or a non-Latin letter | `Add a symbol` |

Length counts Unicode code points, so an emoji counts as one character.

Throws `IllegalArgumentException` when `password` is `null`.

### `record Result(int score, List<String> advice)`

- `score`: passed checks, `0` through `5`.
- `advice`: unmodifiable list of suggestions for the failed checks; empty when `score` is 5. The text is English.

The class is stateless and thread-safe.

## Changes in 1.1.0

- Line breaks no longer hide other characters. In 1.0.1 the regular expressions did not cross line terminators, so a password containing `\n` could lose its case, digit, and symbol points.
- Length is counted in code points instead of UTF-16 units, so emoji count as one character.
- The password is scanned once instead of by four regular expressions.

## License

MIT
