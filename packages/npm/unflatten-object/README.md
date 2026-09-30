# @x-mori/unflatten-object

Expand an object with dot-path keys into nested objects — for form fields named `address.city`, environment-style config, or CSV columns — with protection against prototype pollution.

```js
import { unflattenObject } from '@x-mori/unflatten-object';

unflattenObject({ 'db.host': 'localhost', 'db.port': 5432, debug: true });
// { db: { host: 'localhost', port: 5432 }, debug: true }
```

This reverses [`@x-mori/flatten-object`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/flatten-object).

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/unflatten-object
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

Nested form fields from a request body:

```js
const body = Object.fromEntries(new URLSearchParams('name=Ada&address.city=London&address.zip=N1'));
unflattenObject(body);
// { name: 'Ada', address: { city: 'London', zip: 'N1' } }
```

## API

### `unflattenObject(flat)`

| Parameter | Type | Description |
| --- | --- | --- |
| `flat` | `Record<string, unknown>` | Object whose own enumerable string keys are dot paths. It is not modified. |

Returns a new nested object. Every object it creates has a null prototype; spread it (`{ ...result }`) if you need `Object.prototype` methods.

Throws `TypeError`, and returns nothing, when:

- `flat` is `null`, an array, or not an object;
- a path has an empty segment, such as `'a..b'`, `'.a'`, or `'a.'`;
- a segment is `__proto__`, `constructor`, or `prototype`;
- two paths conflict, such as `'a'` and `'a.b'` (in either order), or the same path appears twice.

## Behavior

- Values are placed as-is. An object supplied as a value is never merged into or changed, even when another path would need to extend it; that case is a conflict error.
- Numeric segments create object keys, not arrays: `{ 'list.0': 'x' }` gives `{ list: { 0: 'x' } }`.

## License

MIT
