# @x-mori/deep-merge-lite

Merge two plain objects deeply into a new object, without changing either input and without prototype pollution. Useful for layering configuration: defaults, then environment settings, then user overrides.

```js
import { deepMergeLite } from '@x-mori/deep-merge-lite';

const defaults = { db: { host: 'localhost', port: 5432 }, tags: ['a'] };
const config = deepMergeLite(defaults, { db: { host: 'db.internal' }, tags: ['b'] });
// { db: { host: 'db.internal', port: 5432 }, tags: ['b'] }
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/deep-merge-lite
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

Merge more than two layers by chaining calls:

```js
const config = [envConfig, userConfig].reduce(deepMergeLite, defaults);
```

Merging parsed JSON from an untrusted source is safe:

```js
deepMergeLite({}, JSON.parse('{"__proto__": {"isAdmin": true}}'));
({}).isAdmin; // undefined
```

## API

### `deepMergeLite(left, right)`

| Parameter | Type | Description |
| --- | --- | --- |
| `left` | plain object | Base values. |
| `right` | plain object | Values that take precedence. |

Returns a new object. It, and every nested object it creates, has a null prototype; spread it (`{ ...result }`) if you need `Object.prototype` methods such as `hasOwnProperty`.

Throws `TypeError` when either argument is not a plain object (an object literal or null-prototype object). Arrays, class instances, and `Date` are rejected.

## Merge rules

| Value in `right` | Value in `left` | Result |
| --- | --- | --- |
| plain object | plain object | merged recursively |
| plain object | anything else | deep copy of the `right` object |
| array | anything | shallow copy of the `right` array (arrays are replaced, not concatenated) |
| anything else | anything | the `right` value, by reference |
| *(key absent)* | any value | the `left` value, copied the same way |

- `undefined` in `right` is a value, so `{ a: undefined }` overwrites `a`.
- The keys `__proto__`, `constructor`, and `prototype` are skipped at every depth.
- Only own enumerable string keys are merged; symbol keys are ignored.
- Inputs must not contain cycles.

## Changes in 1.1.0

- Faster merging: each nested object is now copied once instead of being cloned again at every level where both sides overlap. Results are unchanged.

## License

MIT
