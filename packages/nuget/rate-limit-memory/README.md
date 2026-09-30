# XMori.RateLimitMemory

A small in-memory, fixed-window rate limiter per key — for example, 5 login attempts per minute per IP address.

```csharp
using XMori.RateLimitMemory;

var limiter = new MemoryRateLimiter(limit: 5, window: TimeSpan.FromMinutes(1));

if (!limiter.TryAcquire(clientIp)) return Results.StatusCode(StatusCodes.Status429TooManyRequests);
```

## Install

Targets .NET 8; no dependencies beyond the framework.

The package is published to GitHub Packages, which requires authentication even for public packages. Add the feed once with a token that has the `read:packages` scope, then add the package:

```sh
dotnet nuget add source "https://nuget.pkg.github.com/x-mori/index.json" --name github-x-mori --username YOUR_GITHUB_USERNAME --password YOUR_TOKEN
dotnet add package XMori.RateLimitMemory --version 1.1.0
```

On macOS and Linux, add `--store-password-in-clear-text` to the first command, or keep the token in an environment variable referenced from `nuget.config`.

## How the window works

Each key has its own window, which starts at that key's first request. Within the window, up to `limit` requests are allowed and the rest are refused. Once the window has fully elapsed, the next request starts a new window.

Because windows are fixed, a client can make up to `2 × limit` requests in a short burst that spans the end of one window and the start of the next. Use `System.Threading.RateLimiting.SlidingWindowRateLimiter` if you need a smoother limit.

State lives in this process only: each server instance counts separately, and counts reset when the process restarts. Use a shared store such as Redis to limit across instances.

## Usage

Keys are never removed automatically. When keys are unbounded, such as IP addresses, clean up periodically:

```csharp
using var timer = new PeriodicTimer(TimeSpan.FromMinutes(5));
while (await timer.WaitForNextTickAsync(stoppingToken)) limiter.RemoveExpired();
```

Pass a fixed clock in tests:

```csharp
var start = DateTimeOffset.UnixEpoch;
limiter.TryAcquire("user-1", start);
limiter.TryAcquire("user-1", start.AddSeconds(61)); // new window
```

## API

### `MemoryRateLimiter(int limit, TimeSpan window)`

Allows `limit` requests per key in each `window`. Throws `ArgumentOutOfRangeException` when `limit` is below 1 or `window` is not positive.

### `bool TryAcquire(string key, DateTimeOffset? now = null)`

Uses one slot for `key` and returns `true`, or returns `false` if the key has reached its limit. A refused request does not use a slot. Keys are compared ordinally (case-sensitive). `now` defaults to `DateTimeOffset.UtcNow`. Throws `ArgumentNullException` when `key` is `null`.

### `int RemoveExpired(DateTimeOffset? now = null)`

Removes keys whose window has fully elapsed and returns how many were removed. This only frees memory: a removed key's next request starts a new window either way.

One instance is safe to share between threads.

## Changes in 1.1.0

- `TryAcquire` updates a key with a single dictionary lookup, and `RemoveExpired` no longer builds a temporary array.
- An invalid `window` now reports `window` as the parameter name (it reported `limit`).

## License

MIT
