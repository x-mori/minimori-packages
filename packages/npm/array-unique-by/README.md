# @x-mori/array-unique-by

Remove duplicates from an array by a property or computed key, keeping the first occurrence of each key.

```js
import { arrayUniqueBy } from '@x-mori/array-unique-by';

const users = [{ id: 1, name: 'Ada' }, { id: 2, name: 'Lin' }, { id: 1, name: 'Ada L.' }];
arrayUniqueBy(users, 'id'); // [{ id: 1, name: 'Ada' }, { id: 2, name: 'Lin' }]
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/array-unique-by
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

```js
import { arrayUniqueBy } from '@x-mori/array-unique-by';

// By property name
arrayUniqueBy(orders, 'customerId');

// By computed key: case-insensitive email
arrayUniqueBy(contacts, contact => contact.email.toLowerCase());

// By several fields: combine them into one primitive key
arrayUniqueBy(events, event => `${event.type}:${event.date}`);
```

## API

### `arrayUniqueBy(items, selector)`

| Parameter | Type | Description |
| --- | --- | --- |
| `items` | `readonly T[]` | Items to deduplicate. The array is not modified. |
| `selector` | `keyof T` or `(item: T, index: number) => K` | Property name, or a function returning the key for each item. |

Returns a new `T[]` containing the first item for each key, in the original order.

Throws `TypeError` when `items` is not an array or `selector` is neither a string nor a function.

## Behavior

- Keys are compared with SameValueZero, the rule `Set` uses: `NaN` equals `NaN`, and `0` equals `-0`.
- Objects are compared by reference, so two different objects with the same contents are different keys. Return a string or number from the selector to compare by value.
- A missing property gives the key `undefined`, so all items without it collapse into one.
- Holes in sparse arrays are skipped.

## License

MIT
