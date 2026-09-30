# business-days

Count the weekdays (Monday through Friday) between two dates, inclusive, in constant time.

```java
import io.github.xmori.businessdays.BusinessDays;

long days = BusinessDays.count(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 16)); // 10
```

## Install

Maven coordinates: `io.github.xmori:business-days:1.1.0`. Requires Java 17 or newer; no runtime dependencies.

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
    <artifactId>business-days</artifactId>
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

For Gradle, add `maven("https://maven.pkg.github.com/x-mori/minimori-packages")` with the same credentials, then `implementation("io.github.xmori:business-days:1.1.0")`.

## Usage

Subtract holidays that fall on weekdays yourself:

```java
Set<LocalDate> holidays = Set.of(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 12));
long workingDays = BusinessDays.count(start, end)
        - holidays.stream()
                .filter(day -> !day.isBefore(start) && !day.isAfter(end))
                .filter(day -> day.getDayOfWeek().getValue() <= 5)
                .count();
```

## API

### `static long BusinessDays.count(LocalDate start, LocalDate end)`

Returns the number of Mondays through Fridays from `start` to `end`, **both included**.

| Range | Result |
| --- | --- |
| a single weekday (`start == end`) | `1` |
| a Saturday and Sunday | `0` |
| Monday to the following Sunday | `5` |

The count takes constant time for any range length. Public holidays are not excluded, and the weekend is always Saturday and Sunday.

Throws `IllegalArgumentException` when either date is `null` or `start` is after `end`.

The class is stateless and thread-safe.

## License

MIT
