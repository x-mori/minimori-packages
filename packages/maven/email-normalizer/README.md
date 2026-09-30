# email-normalizer

Normalize email addresses for storage and comparison: trim whitespace and lowercase the domain, while keeping the local part exactly as typed.

```java
import io.github.xmori.emailnormalizer.EmailNormalizer;

EmailNormalizer.normalize("  Jane.Doe@Example.COM "); // "Jane.Doe@example.com"
```

## Install

Maven coordinates: `io.github.xmori:email-normalizer:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>email-normalizer</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:email-normalizer:1.1.0")`.

## Usage

```java
try {
    String email = EmailNormalizer.normalize(form.email());
    users.create(email);
} catch (IllegalArgumentException invalid) {
    errors.add("Please enter a valid email address.");
}
```

## API

### `static String EmailNormalizer.normalize(String email)`

Returns the address with surrounding whitespace removed and the domain lowercased with `Locale.ROOT`.

- The local part (before `@`) keeps its case, because RFC 5321 lets mail servers treat it as case-sensitive. If your system treats it as case-insensitive, lowercase it yourself.
- Provider-specific rules, such as removing dots or `+tag` suffixes for Gmail, are not applied.
- Internationalized domain names are lowercased but not converted to Punycode.

Throws `IllegalArgumentException` when `email` is `null` or fails the structural check: after trimming, it must contain exactly one `@`, with at least one character before and after it, and no whitespace. This is not full RFC 5322 validation, and it does not check that the domain exists or accepts mail.

The class is stateless and thread-safe.

## License

MIT
