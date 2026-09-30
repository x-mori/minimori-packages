# @x-mori/retry-async

Retry a failing async operation with exponential backoff and a fixed attempt limit.

```js
import { retryAsync } from '@x-mori/retry-async';

const data = await retryAsync(() => fetchJson('https://api.example.com/items'), {
  attempts: 4,  // up to 4 calls in total
  delay: 200,   // wait 200 ms, then 400 ms, then 800 ms between them
});
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/retry-async
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

The operation receives the attempt number, starting at 1:

```js
await retryAsync(async attempt => {
  console.log(`attempt ${attempt}`);
  const response = await fetch(url);
  if (!response.ok) throw new Error(`HTTP ${response.status}`); // throw to trigger a retry
  return response.json();
}, { attempts: 3, delay: 500 });
```

Every error is retried. To stop early on errors that will not go away, return or rethrow them outside the retried function:

```js
const result = await retryAsync(async () => {
  const response = await fetch(url);
  if (response.status >= 500) throw new Error('server error'); // retried
  return response; // a 404 is returned, not retried
});
```

## API

### `retryAsync(operation, options?)`

| Option | Default | Description |
| --- | --- | --- |
| `attempts` | `3` | Maximum number of calls, including the first. Positive integer. |
| `delay` | `0` | Milliseconds to wait after the first failure. Zero or more. |
| `factor` | `2` | Multiplier for each later wait. At least `1`; use `1` for a constant delay. |

The wait after failure *n* is `delay × factor^(n−1)` milliseconds, capped at 2147483647 ms (about 24.8 days), the largest delay a timer supports. No wait follows the last attempt.

Returns a promise for the first successful result. `operation` can return a value or a promise, and can throw synchronously.

Rejects with the **last** error thrown by `operation`, unchanged, once all attempts fail. Rejects with `TypeError` when `operation` is not a function or an option is invalid.

## Changes in 1.1.0

- A wait above 2147483647 ms is now capped at that value. Previously, Node.js treated such a timer as 1 ms, so a very large delay retried almost immediately. Large backoff sequences no longer throw `RangeError('retry delay overflow')`.

## License

MIT
