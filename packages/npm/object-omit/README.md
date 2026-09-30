# @x-mori/object-omit

Copy an object without some of its keys — for stripping passwords, internal fields, or database IDs before sending data to a client or a log.

```js
import { objectOmit } from '@x-mori/object-omit';

const user = { id: 7, name: 'Ada', passwordHash: '…' };
objectOmit(user, ['passwordHash']); // { id: 7, name: 'Ada' }
```

The opposite of [`@x-mori/object-pick`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/object-pick).

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/object-omit
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## API

### `objectOmit(object, keys)`

| Parameter | Type | Description |
| --- | --- | --- |
| `object` | `object` | Source object. It is not modified. |
| `keys` | `readonly (keyof T)[]` | Keys to leave out. Keys that are not present are ignored. |

Returns a new object typed as `Omit<T, K>`.

Throws `TypeError` when `object` is `null` or not an object, or `keys` is not an array.

## Behavior

- Own enumerable string **and** symbol keys are copied; inherited and non-enumerable properties are not.
- The copy is shallow: nested objects are shared with the source.
- The result has a null prototype, so an own `__proto__` key (for example from `JSON.parse`) stays an ordinary property. Spread the result (`{ ...rest }`) if you need a normal object, for example before calling `hasOwnProperty` on it.

## License

MIT
