# @x-mori/flatten-object

Turn nested plain objects into a single-level object with dot-path keys — for form libraries, CSV columns, logging fields, or key-value stores.

```js
import { flattenObject } from '@x-mori/flatten-object';

flattenObject({ db: { host: 'localhost', port: 5432 }, tags: ['a'], meta: {} });
// { 'db.host': 'localhost', 'db.port': 5432, tags: ['a'], meta: {} }
```

Reverse it with [`@x-mori/unflatten-object`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/unflatten-object).

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/flatten-object
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## API

### `flattenObject(object)`

| Parameter | Type | Description |
| --- | --- | --- |
| `object` | plain object | Object to flatten. It is not modified. |

Returns a new null-prototype object whose keys are dot paths, in the input's key order.

Throws `TypeError` when `object` is not a plain object, or when any key at any depth:

- is empty (`''`),
- contains a dot (`'a.b'`), because the path would be ambiguous, or
- is `__proto__`, `constructor`, or `prototype`, because expanding it again would be unsafe.

## Behavior

- Only plain objects (object literals and null-prototype objects) are descended into.
- Arrays, empty objects, `Date`, `Map`, class instances, `null`, and other values are kept as leaf values, by reference.
- Only own enumerable string keys are included.
- Inputs must not contain cycles.

## License

MIT
