# initials-from-name

Build uppercase initials from a person's name, for avatar placeholders and compact labels.

```java
import io.github.xmori.initialsfromname.InitialsFromName;

InitialsFromName.of("Ada Lovelace");   // "AL"
InitialsFromName.of("émile zola");     // "ÉZ"
```

## Install

Maven coordinates: `io.github.xmori:initials-from-name:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>initials-from-name</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:initials-from-name:1.1.0")`.

## Usage

Every word contributes a letter. Keep only the first and last for long names:

```java
String all = InitialsFromName.of("Mary Ann Evans"); // "MAE"
String avatar = all.length() <= 2 ? all : "" + all.charAt(0) + all.charAt(all.length() - 1); // "ME"
```

(Use code-point methods instead of `charAt` if names may start with characters outside the Basic Multilingual Plane.)

## API

### `static String InitialsFromName.of(String name)`

Returns the first character of each word, uppercased with `Locale.ROOT`.

- Words are separated by any Unicode whitespace or space character, including tabs, line breaks, the no-break space (U+00A0), and the ideographic space (U+3000). Repeated and surrounding whitespace is ignored.
- Hyphens and apostrophes do not split words: `"jean-luc picard"` gives `"JP"`.
- Characters are Unicode code points, so a first character outside the Basic Multilingual Plane is kept whole.
- A blank name returns `""`.

Throws `IllegalArgumentException` when `name` is `null`.

The class is stateless and thread-safe.

## Changes in 1.1.0

- Names separated by tabs, line breaks, or other Unicode whitespace are now split correctly. In 1.0.1 only the ASCII space separated words, so `"John\tSmith"` gave `"J"`; it now gives `"JS"`.
- Initials are collected in one pass without regular expressions.

## License

MIT
