# XMori.DebouncePromise

Debounce async work: when calls arrive in a burst, run the action **once** with the latest argument after a quiet period, and give every caller in the burst the same result.

```csharp
using XMori.DebouncePromise;

var search = new AsyncDebouncer<string, SearchResult>(query => api.SearchAsync(query), TimeSpan.FromMilliseconds(300));

// Typing "c", "ca", "cat" quickly sends one request, for "cat".
// All three awaits receive that result.
var result = await search.RunAsync(textBox.Text);
```

## Install

Targets .NET 8; no dependencies beyond the framework.

The package is published to GitHub Packages, which requires authentication even for public packages. Add the feed once with a token that has the `read:packages` scope, then add the package:

```sh
dotnet nuget add source "https://nuget.pkg.github.com/x-mori/index.json" --name github-x-mori --username YOUR_GITHUB_USERNAME --password YOUR_TOKEN
dotnet add package XMori.DebouncePromise --version 1.1.0
```

On macOS and Linux, add `--store-password-in-clear-text` to the first command, or keep the token in an environment variable referenced from `nuget.config`.

## How it works

```text
RunAsync("c")   ─┐
RunAsync("ca")  ─┼─ each call restarts the 300 ms timer
RunAsync("cat") ─┘
                 └── 300 ms of quiet ──► action("cat") ──► all three tasks complete with its result
```

- Every call restarts the quiet-period timer and replaces the pending argument.
- When the timer expires, the action runs once with the latest argument.
- If the action throws, every task in the burst faults with that exception.
- Calls made after the action has started begin a new burst; they do not receive the running action's result.

## API

### `AsyncDebouncer<TArg, TResult>(Func<TArg, Task<TResult>> action, TimeSpan delay)`

Creates a debouncer. `delay` is the quiet period and must be zero or greater. Throws `ArgumentNullException` for a `null` action and `ArgumentOutOfRangeException` for a negative delay.

### `Task<TResult> RunAsync(TArg argument)`

Schedules the action with `argument` and returns the task shared by the current burst.

One instance is safe to use from multiple threads. With a positive delay the action starts on the thread pool; with a zero delay it may start on the calling thread. No lock is held while the action runs.

## Changes in 1.1.0

- A superseded call no longer throws and catches a `TaskCanceledException` internally, which removes one exception per debounced call.
- The action is never started while the debouncer's internal lock is held, even with a zero delay.
- Finished timers are disposed as soon as their action starts.

## License

MIT
