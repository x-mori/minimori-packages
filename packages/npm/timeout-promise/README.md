# @x-mori/timeout-promise

Reject a promise that takes too long, with your own error message.

```js
import { timeoutPromise } from '@x-mori/timeout-promise';

const rows = await timeoutPromise(db.query(sql), 2_000, 'Database query took longer than 2 s');
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/timeout-promise
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

`timeoutPromise` stops **waiting**, but it cannot stop the underlying work. When the operation supports cancellation, give it a signal as well:

```js
const response = await timeoutPromise(
  fetch(url, { signal: AbortSignal.timeout(5_000) }), // cancels the request itself
  5_000,
  `GET ${url} timed out`,
);
```

Tell a timeout apart from other failures by its message:

```js
try {
  await timeoutPromise(task(), 1_000, 'task timeout');
} catch (error) {
  if (error.message === 'task timeout') retryLater(); else throw error;
}
```

## API

### `timeoutPromise(value, ms, message?)`

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `PromiseLike<T> \| T` | Promise, thenable, or plain value to wait for. |
| `ms` | `number` | Deadline in milliseconds, from `0` through `2147483647` (about 24.8 days). |
| `message` | `string` | Message of the timeout error. Default `'Operation timed out'`. |

Returns `Promise<T>` that settles like `value` if it settles first, or rejects with `new Error(message)` when the deadline passes. The timer is cleared as soon as either happens, so a fast operation does not keep the process running.

Throws `RangeError` synchronously when `ms` is negative, not finite, or above `2147483647`.

## Changes in 1.1.0

- Deadlines above 2147483647 ms now throw `RangeError`. Previously Node.js treated such a timer as 1 ms, so the promise timed out almost immediately.

## License

MIT
