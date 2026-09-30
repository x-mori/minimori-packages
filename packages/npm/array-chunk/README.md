# @x-mori/array-chunk

Split an array into consecutive chunks of a fixed maximum size — for batching API calls, database inserts, or paging through results.

```js
import { arrayChunk } from '@x-mori/array-chunk';

arrayChunk([1, 2, 3, 4, 5], 2); // [[1, 2], [3, 4], [5]]
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/array-chunk
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

```js
import { arrayChunk } from '@x-mori/array-chunk';

// Insert rows 500 at a time.
for (const batch of arrayChunk(rows, 500)) {
  await db.insert('events', batch);
}

// Run at most 10 requests in parallel, batch by batch.
for (const ids of arrayChunk(userIds, 10)) {
  await Promise.all(ids.map(id => fetchUser(id)));
}
```

## API

### `arrayChunk(items, size)`

| Parameter | Type | Description |
| --- | --- | --- |
| `items` | `readonly T[]` | Array to split. It is not modified. |
| `size` | `number` | Maximum chunk length. Must be a positive safe integer. |

Returns `T[][]`: new arrays, each of exactly `size` items except possibly the last. Items keep their order. An empty input returns `[]`.

Throws `TypeError` when `items` is not an array or `size` is not a positive safe integer (`0`, `1.5`, and `NaN` are rejected).

## License

MIT
