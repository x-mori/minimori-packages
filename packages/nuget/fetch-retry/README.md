# XMori.FetchRetry

Retry HTTP GET requests after transient failures — HTTP 429, any 5xx, or a network error — with exponential backoff.

```csharp
using XMori.FetchRetry;

using var response = await RetryingFetch.GetAsync(client, new Uri("https://api.example.com/status"));
response.EnsureSuccessStatusCode();
```

## Install

Targets .NET 8; no dependencies beyond the framework.

The package is published to GitHub Packages, which requires authentication even for public packages. Add the feed once with a token that has the `read:packages` scope, then add the package:

```sh
dotnet nuget add source "https://nuget.pkg.github.com/x-mori/index.json" --name github-x-mori --username YOUR_GITHUB_USERNAME --password YOUR_TOKEN
dotnet add package XMori.FetchRetry --version 1.1.0
```

On macOS and Linux, add `--store-password-in-clear-text` to the first command, or keep the token in an environment variable referenced from `nuget.config`.

## Usage

```csharp
// Up to 5 requests, waiting 0.5 s, 1 s, 2 s, 4 s between them.
using var response = await RetryingFetch.GetAsync(client, uri, attempts: 5, delay: TimeSpan.FromMilliseconds(500), cancellationToken: token);
```

## API

### `static Task<HttpResponseMessage> RetryingFetch.GetAsync(HttpClient client, Uri uri, int attempts = 3, TimeSpan? delay = null, CancellationToken cancellationToken = default)`

| Parameter | Default | Description |
| --- | --- | --- |
| `attempts` | `3` | Maximum number of requests, including the first. At least 1. |
| `delay` | 200 ms | Wait before the second request. Each later wait doubles, up to 30 seconds. |
| `cancellationToken` | none | Cancels the current request or backoff wait. |

**What is retried:** HTTP 429 (Too Many Requests), any status of 500 or above, and `HttpRequestException` (connection failures, DNS errors, and so on). Other statuses, including 4xx, are returned at once because repeating them will not help.

**Returns** the first response that is not retried, or the last response once attempts run out, even if it is a failure status. Check the status yourself. You own the response and must dispose it; responses that triggered a retry are disposed for you.

**Throws**

- `ArgumentNullException` for a `null` client or URI, and `ArgumentOutOfRangeException` when `attempts` is below 1 or `delay` is negative.
- `HttpRequestException` when the final attempt fails without a response.
- `OperationCanceledException` when the token is cancelled or the client's timeout expires. Timeouts are not retried.

Notes:

- A `Retry-After` header is not read. Choose a `delay` that suits the server's rate limits.
- Only GET is sent, so retrying is safe for servers that follow HTTP semantics.

The class is stateless and thread-safe.

## Changes in 1.1.0

- A zero `delay` no longer schedules an empty timer between attempts.

## License

MIT
