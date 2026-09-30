# @x-mori/slugify-lite

Turn text into a lowercase, hyphenated ASCII slug for URLs, file names, and HTML IDs.

```js
import { slugifyLite } from '@x-mori/slugify-lite';

slugifyLite('  Hello, World!  ');   // 'hello-world'
slugifyLite('Crème Brûlée & Café'); // 'creme-brulee-cafe'
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/slugify-lite
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

Text with no ASCII letters or digits produces an empty string, so give it a fallback:

```js
const slug = slugifyLite(post.title) || `post-${post.id}`;
```

Slugs are not unique. If two titles can produce the same slug, add a suffix or an ID.

## API

### `slugifyLite(text)`

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `string` | Text to convert. |

Returns a string containing only `a`–`z`, `0`–`9`, and single hyphens, with no hyphen at either end. It may be empty.

Throws `TypeError` when `text` is not a string.

## How text is converted

1. Unicode NFKD normalization splits accented letters into a base letter and accent marks, and turns compatibility characters such as `ﬁ` and full-width `Ａ` into plain letters.
2. Combining accent marks are removed (`é` → `e`).
3. The text is lowercased.
4. Every run of characters other than `a`–`z` and `0`–`9` becomes one hyphen. This includes spaces, punctuation, emoji, and letters from other scripts.
5. Hyphens at the start and end are removed.

Letters that do not decompose to ASCII, such as `ß`, `ø`, and `ł`, are treated like punctuation rather than transliterated. Use a transliteration library first if you need those.

## License

MIT
