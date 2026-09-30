# age-from-date

Calculate a person's age in completed years from a birth date, with correct handling of birthdays later in the year and 29 February.

```java
import io.github.xmori.agefromdate.AgeFromDate;

int age = AgeFromDate.years(LocalDate.of(1990, 7, 15), LocalDate.of(2026, 7, 14)); // 35
```

## Install

Maven coordinates: `io.github.xmori:age-from-date:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>age-from-date</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:age-from-date:1.1.0")`.

## Usage

Pass today's date in the time zone that matters to you. Near midnight, "today" differs between zones:

```java
int age = AgeFromDate.years(user.birthDate(), LocalDate.now(ZoneId.of("Asia/Tokyo")));
boolean adult = age >= 18;
```

## API

### `static int AgeFromDate.years(LocalDate birthDate, LocalDate today)`

Returns the number of completed years from `birthDate` to `today`.

- A birthday that has not yet occurred in `today`'s year does not count.
- `birthDate` equal to `today` gives `0`.
- Someone born on 29 February turns a year older on 1 March in common years, the same rule `java.time.Period` uses.

Throws `IllegalArgumentException` when either date is `null` or `birthDate` is after `today`.

The class is stateless and thread-safe.

## License

MIT
