# XMori.FetchJsonSafe

Send a JSON GET request and get a result object back instead of exceptions: success with typed data, or failure with the status and a message.

```csharp
using XMori.FetchJsonSafe;

var result = await JsonFetch.GetAsync<User>(client, new Uri("https://api.example.com/users/7"));
if (result.Success) Console.WriteLine(result.Data!.Name);
else Console.WriteLine($"Failed ({result.Status}): {result.Error}");
```

## Install

Targets .NET 8; no dependencies beyond the framework.

The package is published to GitHub Packages, which requires authentication even for public packages. Add the feed once with a token that has the `read:packages` scope, then add the package:

```sh
dotnet nuget add source "https://nuget.pkg.github.com/x-mori/index.json" --name github-x-mori --username YOUR_GITHUB_USERNAME --password YOUR_TOKEN
dotnet add package XMori.FetchJsonSafe --version 1.1.0
```

On macOS and Linux, add `--store-password-in-clear-text` to the first command, or keep the token in an environment variable referenced from `nuget.config`.

## API

### `static Task<JsonFetchResult<T>> JsonFetch.GetAsync<T>(HttpClient client, Uri uri, CancellationToken cancellationToken = default)`

Sends a GET request with `client` (its base address, default headers, and timeout apply) and reads a successful response as JSON with `System.Text.Json` web defaults: camelCase names, case-insensitive matching.

| Outcome | `Success` | `Data` | `Status` | `Error` |
| --- | --- | --- | --- | --- |
| 2xx with valid JSON | `true` | the value (`default` for a JSON `null`) | the status | `null` |
| non-2xx status | `false` | `default` | the status | `"HTTP 404"` and so on; the body is not read |
| 2xx with invalid JSON or an unsupported content type | `false` | `default` | the status | the parser's message |
| network error | `false` | `default` | `null` | the exception message |
| `HttpClient.Timeout` expired | `false` | `default` | `null` | the exception message |

Throws:

- `ArgumentNullException` when `client` or `uri` is `null`.
- `OperationCanceledException` when **your** `cancellationToken` is cancelled. Caller cancellation is always rethrown, never turned into a result.

### `record JsonFetchResult<T>(bool Success, T? Data, HttpStatusCode? Status, string? Error)`

The outcome described above.

The class is stateless and thread-safe; reuse one `HttpClient` (or use `IHttpClientFactory`) as usual.

## Changes in 1.1.0

- An `HttpClient.Timeout` expiry now returns a failure result, as the documentation promised. In 1.0.1 it escaped as a `TaskCanceledException`.
- When a successful response has an invalid JSON body, `Status` now holds the response status instead of `null`.

## License

MIT
