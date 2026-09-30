# XMori.FetchTimeout

Give one HTTP GET request its own deadline, without changing the `Timeout` of a shared `HttpClient`.

```csharp
using XMori.FetchTimeout;

using var response = await TimedFetch.GetAsync(client, new Uri("https://example.com/health"), TimeSpan.FromSeconds(2));
```

## Install

Targets .NET 8; no dependencies beyond the framework.

The package is published to GitHub Packages, which requires authentication even for public packages. Add the feed once with a token that has the `read:packages` scope, then add the package:

```sh
dotnet nuget add source "https://nuget.pkg.github.com/x-mori/index.json" --name github-x-mori --username YOUR_GITHUB_USERNAME --password YOUR_TOKEN
dotnet add package XMori.FetchTimeout --version 1.1.0
```

On macOS and Linux, add `--store-password-in-clear-text` to the first command, or keep the token in an environment variable referenced from `nuget.config`.

## Usage

A timeout and a caller cancellation both throw `OperationCanceledException`. Check your own token to tell them apart:

```csharp
try {
    using var response = await TimedFetch.GetAsync(client, uri, TimeSpan.FromSeconds(2), cancellationToken);
    return response.IsSuccessStatusCode;
} catch (OperationCanceledException) when (!cancellationToken.IsCancellationRequested) {
    return false; // timed out
}
```

## API

### `static Task<HttpResponseMessage> TimedFetch.GetAsync(HttpClient client, Uri uri, TimeSpan timeout, CancellationToken cancellationToken = default)`

Sends a GET request that is cancelled if the complete response, headers and body, has not arrived within `timeout`.

- Returns the response with its body already buffered, including non-success statuses. You must dispose it.
- The shorter of `timeout` and `client.Timeout` applies.
- Cancelling `cancellationToken` also cancels the request.

Throws:

- `ArgumentNullException` for a `null` client or URI, and `ArgumentOutOfRangeException` when `timeout` is zero or negative.
- `OperationCanceledException` (usually `TaskCanceledException`) when the deadline passes or the caller cancels.
- `HttpRequestException` for network and protocol errors.

The class is stateless and thread-safe.

## License

MIT
