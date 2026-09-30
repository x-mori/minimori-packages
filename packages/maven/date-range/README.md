# date-range

Generate a list of dates from a start to an end date, stepping by days, weeks, or months. Monthly steps stay on the original day of the month where it exists.

```java
import io.github.xmori.daterange.DateRange;

DateRange.between(LocalDate.of(2026, 1, 31), LocalDate.of(2026, 4, 30), ChronoUnit.MONTHS);
// [2026-01-31, 2026-02-28, 2026-03-31, 2026-04-30]
```

## Install

Maven coordinates: `io.github.xmori:date-range:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>date-range</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:date-range:1.1.0")`.

## Usage

```java
// Every day in January
List<LocalDate> days = DateRange.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31), ChronoUnit.DAYS);

// Every Monday in a quarter (start on a Monday)
List<LocalDate> mondays = DateRange.between(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 3, 31), ChronoUnit.WEEKS);
```

## API

### `static List<LocalDate> DateRange.between(LocalDate start, LocalDate end, ChronoUnit unit)`

Returns the dates `start`, `start + 1 unit`, `start + 2 units`, and so on, up to and including `end`.

- `unit` must be `ChronoUnit.DAYS`, `WEEKS`, or `MONTHS`.
- `start` is always included. `end` is included only when it falls on a step.
- Every date is computed from `start` (`start.plus(n, unit)`), so month steps do not drift: a range starting on 31 January continues with the last day of February, then 31 March.
- The list is unmodifiable and held fully in memory.

Throws `IllegalArgumentException` when an argument is `null`, `start` is after `end`, or `unit` is not supported.

The class is stateless and thread-safe.

## Changes in 1.1.0

- Month steps no longer drift. In 1.0.1 each date was computed from the previous one, so a range from 31 January gave 28 February, **28** March, **28** April. It now gives 28 February, 31 March, 30 April.

## License

MIT
