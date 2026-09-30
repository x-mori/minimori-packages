# @x-mori/sleep-promise

Wait for a number of milliseconds with `await`, with optional cancellation through an `AbortSignal`.

```js
import { sleepPromise } from '@x-mori/sleep-promise';

await sleepPromise(500); // continue after half a second
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/sleep-promise
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

Cancel a wait, for example when a server shuts down:

```js
const controller = new AbortController();
process.once('SIGTERM', () => controller.abort());

try {
  while (true) {
    await pollQueue();
    await sleepPromise(5_000, { signal: controller.signal });
  }
} catch (error) {
  if (error.name !== 'AbortError') throw error;
}
```

Pass your own reason to `abort()` and it becomes the rejection value:

```js
controller.abort(new Error('shutting down'));
```

## API

### `sleepPromise(ms, options?)`

| Parameter | Type | Description |
| --- | --- | --- |
| `ms` | `number` | Delay in milliseconds, from `0` through `2147483647` (about 24.8 days). Fractions are allowed. |
| `options.signal` | `AbortSignal` | Cancels the wait. |

Returns `Promise<void>`, which resolves after the delay.

- If the signal aborts first, the promise rejects with `signal.reason`, or with a `DOMException` named `AbortError` when no reason was given, and the timer is cleared.
- If the signal is already aborted, the promise rejects immediately and no timer starts.

Throws `RangeError` synchronously when `ms` is negative, not finite, or above `2147483647`.

## Changes in 1.1.0

- Delays above 2147483647 ms now throw `RangeError`. Previously Node.js treated such a timer as 1 ms, so a very long sleep ended almost immediately.

## License

MIT
