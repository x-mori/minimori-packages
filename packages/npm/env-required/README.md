# @x-mori/env-required

Read required environment variables at startup and fail once with a clear message listing **every** missing name.

```js
import { envRequired } from '@x-mori/env-required';

const { DATABASE_URL, API_KEY } = envRequired(['DATABASE_URL', 'API_KEY']);
// If both are unset:
// Error: Missing required environment variables: DATABASE_URL, API_KEY
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/env-required
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

```js
// A single name works too.
const { PORT } = envRequired('PORT');

// Pass any mapping instead of process.env, which keeps tests independent of the real environment.
envRequired(['TOKEN'], { TOKEN: 'test-token' }); // { TOKEN: 'test-token' }
```

With TypeScript, the result is typed from the names you pass:

```ts
const env = envRequired(['DATABASE_URL', 'PORT']);
env.DATABASE_URL; // string
```

## API

### `envRequired(names, source?)`

| Parameter | Type | Description |
| --- | --- | --- |
| `names` | `string \| readonly string[]` | Name, or names, that must be set. |
| `source` | `Record<string, string \| undefined>` | Mapping to read. Defaults to `process.env`. |

Returns a new object containing exactly the requested names and their string values.

Throws:

- `Error` when any variable is absent, `undefined`, or an empty string. The message names all of them: `Missing required environment variable: PORT` or `Missing required environment variables: A, B`.
- `TypeError` when `names` is not a non-empty string or an array of non-empty strings.

Only own properties of `source` are read, so names such as `toString` are never taken from the prototype.

## Changes in 1.1.0

- A key present in `source` with the value `undefined` now counts as missing, and the TypeScript return type is `Record<Name, string>`.

## License

MIT
