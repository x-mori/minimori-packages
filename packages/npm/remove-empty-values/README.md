# @x-mori/remove-empty-values

Copy an object without its `null` and `undefined` values (and optionally empty strings) — for cleaning query parameters, form data, or API payloads.

```js
import { removeEmptyValues } from '@x-mori/remove-empty-values';

removeEmptyValues({ name: 'Ada', email: null, phone: undefined, age: 0 });
// { name: 'Ada', age: 0 }
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/remove-empty-values
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

```js
// Build a query string from optional filters.
const params = removeEmptyValues({ q: form.q, page: form.page, sort: form.sort }, { emptyStrings: true });
const url = `/search?${new URLSearchParams(params)}`;
```

## API

### `removeEmptyValues(object, options?)`

| Parameter | Type | Description |
| --- | --- | --- |
| `object` | `object` | Non-array object to filter. It is not modified. |
| `options.emptyStrings` | `boolean` | Also remove `''`. Default `false`. |

Returns a new plain object typed as `Partial<T>`.

Throws `TypeError` when `object` is `null`, an array, or not an object.

## Behavior

| Value | Removed? |
| --- | --- |
| `null`, `undefined` | always |
| `''` | only with `emptyStrings: true` |
| `0`, `false`, `NaN`, `' '` | never |
| `[]`, `{}` | never |

- Only the top level is filtered. Nested objects and arrays are kept as they are and shared with the input.
- Only own enumerable string keys are copied.

## License

MIT
