# @x-mori/group-by-key

Group array items into a `Map` by a property or computed key. Keys keep their type, so numbers, objects, and symbols work as group keys.

```js
import { groupByKey } from '@x-mori/group-by-key';

const orders = [{ id: 1, status: 'paid' }, { id: 2, status: 'open' }, { id: 3, status: 'paid' }];
const byStatus = groupByKey(orders, 'status');
byStatus.get('paid'); // [{ id: 1, status: 'paid' }, { id: 3, status: 'paid' }]
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/group-by-key
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

```js
// Computed keys
groupByKey([1.2, 1.8, 2.5], n => Math.floor(n)); // Map { 1 => [1.2, 1.8], 2 => [2.5] }

// Group by day
const byDay = groupByKey(events, event => event.at.toISOString().slice(0, 10));

// Convert to a plain object when the keys are strings
const counts = Object.fromEntries([...byStatus].map(([status, items]) => [status, items.length]));
// { paid: 2, open: 1 }
```

## API

### `groupByKey(items, selector)`

| Parameter | Type | Description |
| --- | --- | --- |
| `items` | `readonly T[]` | Items to group. The array is not modified. |
| `selector` | `keyof T` or `(item: T, index: number) => K` | Property name, or a function returning the group key for each item. |

Returns `Map<K, T[]>`. Groups appear in the order their key was first seen, and items keep their input order within each group.

Throws `TypeError` when `items` is not an array or `selector` is neither a string nor a function.

## Behavior

- Keys are compared with SameValueZero, the rule `Map` uses. `1` and `'1'` are different groups; objects are compared by reference.
- A missing property gives the key `undefined`.
- Holes in sparse arrays are skipped.

## Changes in 1.1.0

- Grouping does fewer `Map` lookups per item (one `get`, plus a `set` for each new key), which speeds up large inputs. Results are unchanged.

## License

MIT
