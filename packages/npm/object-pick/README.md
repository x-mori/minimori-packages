# @x-mori/object-pick

Copy only the keys you list from an object — for building API responses from larger records, or selecting allowed fields from request input.

```js
import { objectPick } from '@x-mori/object-pick';

const user = { id: 7, name: 'Ada', passwordHash: '…' };
objectPick(user, ['id', 'name']); // { id: 7, name: 'Ada' }
```

The opposite of [`@x-mori/object-omit`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/object-omit).

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/object-pick
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

Allow-list fields from untrusted input:

```js
const update = objectPick(request.body, ['displayName', 'bio']);
await db.users.update(userId, { ...update });
```

## API

### `objectPick(object, keys)`

| Parameter | Type | Description |
| --- | --- | --- |
| `object` | `object` | Source object. It is not modified. |
| `keys` | `readonly (keyof T)[]` | Keys to copy, in the order they should appear. |

Returns a new object typed as `Pick<T, K>`.

Throws `TypeError` when `object` is `null` or not an object, or `keys` is not an array.

## Behavior

- A key is copied only if it is an **own, enumerable** property of `object`. Missing, inherited (such as `toString`), and non-enumerable keys are skipped, so the result can have fewer keys than requested.
- String and symbol keys are both supported.
- The copy is shallow: nested objects are shared with the source.
- The result has a null prototype, so picking `__proto__` from parsed JSON creates an ordinary property and cannot change any prototype. Spread the result (`{ ...picked }`) if you need a normal object.

## Changes in 1.1.0

- Non-enumerable properties are no longer copied, matching the documented behavior. Previously any own property was copied.

## License

MIT
