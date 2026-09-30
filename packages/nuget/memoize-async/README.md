# XMori.MemoizeAsync

Cache the results of an async function by key for a fixed time, and make concurrent callers for the same key share one in-flight call.

```csharp
using XMori.MemoizeAsync;

var users = new AsyncMemoizer<int, User>(id => api.GetUserAsync(id), TimeSpan.FromMinutes(5));

var user = await users.GetAsync(7); // calls the API
var same = await users.GetAsync(7); // served from the cache for five minutes
```

## Install

Targets .NET 8; no dependencies beyond the framework.

The package is published to GitHub Packages, which requires authentication even for public packages. Add the feed once with a token that has the `read:packages` scope, then add the package:

```sh
dotnet nuget add source "https://nuget.pkg.github.com/x-mori/index.json" --name github-x-mori --username YOUR_GITHUB_USERNAME --password YOUR_TOKEN
dotnet add package XMori.MemoizeAsync --version 1.1.0
```

On macOS and Linux, add `--store-password-in-clear-text` to the first command, or keep the token in an environment variable referenced from `nuget.config`.

## Behavior

- **Shared in-flight work:** when 100 requests ask for key `7` at once, the factory runs once and all 100 await the same task.
- **Successes are cached** for the time-to-live, measured from when the factory call started.
- **Failures are not cached:** if the factory throws or its task faults or is cancelled, every caller sharing that attempt receives the exception, and the next call runs the factory again.
- **Monotonic clock:** changing the system time does not expire or extend entries.
- **No background cleanup:** an expired entry is replaced when its key is requested again. With an unbounded key space, call `Clear()` periodically or use a size-limited cache such as `Microsoft.Extensions.Caching.Memory` instead.

## API

### `AsyncMemoizer<TKey, TValue>(Func<TKey, Task<TValue>> factory, TimeSpan ttl)`

Creates a memoizer. Throws `ArgumentNullException` for a `null` factory and `ArgumentOutOfRangeException` when `ttl` is zero or negative. Keys are compared with the default equality comparer.

### `Task<TValue> GetAsync(TKey key)`

Returns the cached result for `key`, or runs the factory and caches its result. A cache hit on a completed result returns the stored task directly, without allocating. Throws `ArgumentNullException` when `key` is `null`.

### `void Clear()`

Removes every entry. Work already in flight continues, and its callers still receive its result.

One instance is safe to share between threads.

## Changes in 1.1.0

- Cache hits on completed results return the stored task without an extra `async` state machine.
- Expiry uses `Stopwatch` timestamps instead of `DateTimeOffset.UtcNow`, so system clock changes no longer affect it.
- A `null` key throws `ArgumentNullException` immediately rather than through the returned task.

## License

MIT
