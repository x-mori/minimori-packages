# @x-mori/safe-json-parse

Parse JSON without `try`/`catch`: get back either the data or the parse error.

```js
import { safeJsonParse } from '@x-mori/safe-json-parse';

const { data, error } = safeJsonParse(requestBody);
if (error) return res.status(400).send('Invalid JSON');
handle(data);
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/safe-json-parse
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

With TypeScript, checking `error` narrows the result:

```ts
type Settings = { theme: 'light' | 'dark' };

const result = safeJsonParse<Settings>(localStorage.getItem('settings') ?? '');
const theme = result.error === null ? result.data.theme : 'light';
```

The type parameter is not validated at runtime. Validate data from untrusted sources with a schema library before relying on its shape.

A reviver works as it does with `JSON.parse`:

```js
safeJsonParse('{"at":"2026-01-01T00:00:00Z"}', (key, value) => key === 'at' ? new Date(value) : value);
```

## API

### `safeJsonParse(text, reviver?)`

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `string` | JSON text. Other values are converted to a string the way `JSON.parse` converts them. |
| `reviver` | `(key, value) => unknown` | Optional `JSON.parse` reviver. |

Returns one of:

| Outcome | Result |
| --- | --- |
| valid JSON | `{ data: <parsed value>, error: null }` |
| invalid JSON | `{ data: null, error: <SyntaxError> }` |
| reviver throws | `{ data: null, error: <what it threw> }` |

It never throws. Always check `error`, not `data`: the valid JSON text `null` gives `{ data: null, error: null }`.

## License

MIT
