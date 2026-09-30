# @x-mori/safe-get

Read a deeply nested property with a dot path and a fallback, without throwing when a parent is missing and without ever reading from the prototype chain.

```js
import { safeGet } from '@x-mori/safe-get';

const config = { db: { port: 0, hosts: ['a', 'b'] } };
safeGet(config, 'db.port', 5432);   // 0  (present, so the fallback is not used)
safeGet(config, 'db.hosts.1');      // 'b'
safeGet(config, 'cache.ttl', 60);   // 60
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/safe-get
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

Use an array path when a key contains a dot or is a symbol:

```js
safeGet({ 'example.com': { ttl: 30 } }, ['example.com', 'ttl']); // 30
safeGet(record, [Symbol.for('meta'), 'id']);
```

With TypeScript, pass the expected type (it is not checked at runtime):

```ts
const port = safeGet<number>(config, 'db.port', 5432);
```

## API

### `safeGet(object, path, fallback?)`

| Parameter | Type | Description |
| --- | --- | --- |
| `object` | `unknown` | Value to read from. May be `null`, `undefined`, or a primitive. |
| `path` | `string \| readonly PropertyKey[]` | Dot-separated path, or an array of keys. |
| `fallback` | `T` | Returned when the path is missing. Default `undefined`. |

Returns the value at the path, or `fallback`.

Throws `TypeError` when `path` is not a string or array, or contains an empty segment (`'a..b'`, `''`).

## Behavior

- The fallback is used only when a segment does not exist or a parent is `null`/`undefined`. A property that exists with the value `undefined`, `null`, `0`, or `''` is returned as-is.
- Only **own** properties are followed, so `'constructor'`, `'__proto__'`, and `'toString'` return the fallback instead of built-ins.
- Array indexes and own properties of primitives work: `safeGet('abc', 'length')` is `3`.
- Getters on the path are called.

## License

MIT
