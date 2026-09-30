# XMori.AsyncQueueLite

Limit how many asynchronous operations run at the same time — for example, at most 4 HTTP requests or database calls in flight.

```csharp
using XMori.AsyncQueueLite;

using var queue = new AsyncQueue(4);
var pages = await Task.WhenAll(urls.Select(url => queue.EnqueueAsync(() => client.GetStringAsync(url))));
```

## Install

Targets .NET 8; no dependencies beyond the framework.

The package is published to GitHub Packages, which requires authentication even for public packages. Add the feed once with a token that has the `read:packages` scope, then add the package:

```sh
dotnet nuget add source "https://nuget.pkg.github.com/x-mori/index.json" --name github-x-mori --username YOUR_GITHUB_USERNAME --password YOUR_TOKEN
dotnet add package XMori.AsyncQueueLite --version 1.1.0
```

On macOS and Linux, add `--store-password-in-clear-text` to the first command, or keep the token in an environment variable referenced from `nuget.config`.

## Usage

The cancellation token only cancels **waiting for a slot**. Capture it in the lambda if the operation itself should stop too:

```csharp
await queue.EnqueueAsync(() => client.GetStringAsync(url, cancellationToken), cancellationToken);
```

## API

### `AsyncQueue(int concurrency)`

Creates a queue that runs at most `concurrency` operations at once. Throws `ArgumentOutOfRangeException` when `concurrency` is less than 1.

### `Task<T> EnqueueAsync<T>(Func<Task<T>> action, CancellationToken cancellationToken = default)`

Waits for a free slot without blocking a thread, runs `action`, and frees the slot when the operation completes, faults, or is cancelled.

- Returns the operation's result; its exceptions propagate unchanged.
- Waiting callers are not guaranteed to start in first-in, first-out order.
- Throws `ArgumentNullException` for a `null` action, `OperationCanceledException` if the token is cancelled while waiting, and `ObjectDisposedException` after the queue is disposed.

### `void Dispose()`

Releases the underlying `SemaphoreSlim` **immediately**. It does not wait for queued or running operations: await their tasks before disposing. An operation still running at that moment throws `ObjectDisposedException` when it tries to free its slot.

One instance is safe to share between threads.

## Changes in 1.1.0

- The documentation for `Dispose` now describes what it does. It previously claimed to wait for queued operations, which it never did.

## License

MIT
