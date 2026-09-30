# XMori.ApiErrorNormalizer

Turn `HttpRequestException`, cancellations, and other exceptions into one small record — kind, message, and HTTP status — for consistent logging and API error responses.

```csharp
using XMori.ApiErrorNormalizer;

try { await client.GetStringAsync(uri); }
catch (Exception exception) {
    var error = ApiErrors.Normalize(exception);
    logger.LogWarning("{Kind} {Status}: {Message}", error.Kind, error.StatusCode, error.Message);
}
```

## Install

Targets .NET 8; no dependencies beyond the framework.

The package is published to GitHub Packages, which requires authentication even for public packages. Add the feed once with a token that has the `read:packages` scope, then add the package:

```sh
dotnet nuget add source "https://nuget.pkg.github.com/x-mori/index.json" --name github-x-mori --username YOUR_GITHUB_USERNAME --password YOUR_TOKEN
dotnet add package XMori.ApiErrorNormalizer --version 1.1.0
```

On macOS and Linux, add `--store-password-in-clear-text` to the first command, or keep the token in an environment variable referenced from `nuget.config`.

## API

### `record NormalizedApiError(string Message, int? StatusCode, string Kind)`

| Property | Description |
| --- | --- |
| `Message` | The exception message, or the message you passed to `FromStatus`. |
| `StatusCode` | HTTP status code when known; otherwise `null`. |
| `Kind` | `"http"`, `"cancelled"`, or `"unknown"`. |

### `static NormalizedApiError ApiErrors.Normalize(Exception error)`

| Exception | `Kind` | `StatusCode` |
| --- | --- | --- |
| `HttpRequestException` | `"http"` | the response status, or `null` for network failures with no response |
| `OperationCanceledException` (including `TaskCanceledException` from an `HttpClient` timeout) | `"cancelled"` | `null` |
| anything else | `"unknown"` | `null` |

Throws `ArgumentNullException` when `error` is `null`.

The message is copied from the exception unchanged. Do not send it to untrusted clients if it may contain internal details such as host names.

### `static NormalizedApiError ApiErrors.FromStatus(HttpStatusCode status, string? message = null)`

Builds an `"http"` error from a status code, for example after checking a response yourself:

```csharp
if (!response.IsSuccessStatusCode) return ApiErrors.FromStatus(response.StatusCode); // Message "HTTP 404"
```

The default message is `HTTP {code}`.

Both methods are stateless and thread-safe.

## License

MIT
