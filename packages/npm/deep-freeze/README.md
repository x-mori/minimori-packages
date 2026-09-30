# @x-mori/deep-freeze

Recursively freeze an object and everything inside it, so configuration and shared state cannot be changed by accident.

```js
import { deepFreeze } from '@x-mori/deep-freeze';

const config = deepFreeze({ db: { host: 'localhost' }, features: ['search'] });
config.db.host = 'prod';       // ignored (throws a TypeError in strict mode and ES modules)
config.features.push('beta');  // TypeError: Cannot add property 1, object is not extensible
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/deep-freeze
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## API

### `deepFreeze(value)`

Freezes `value` and every object and array reachable from it **in place**, then returns the same reference. Primitives are returned unchanged.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `T` | Value to freeze. |

Returns `Readonly<T>`. The type marks only the top level as readonly; nested objects are frozen at runtime too.

## Behavior

- Own string **and** symbol keys are followed, including non-enumerable ones.
- Shared references and cycles are handled; each object is visited once.
- Functions are returned as-is and are not descended into, so freezing a config that holds a class or callback does not freeze that class's prototype.
- JavaScript cannot freeze a non-empty typed array or `DataView`; those are left writable.
- `Object.freeze` does not reach the internal contents of `Map`, `Set`, or `Date`, so `map.set()` still works on a frozen `Map`.
- Accessor properties (getters) are read once while walking, and the returned value is frozen.

## License

MIT
