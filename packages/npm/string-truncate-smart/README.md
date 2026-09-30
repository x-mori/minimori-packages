# @x-mori/string-truncate-smart

Shorten text to a maximum length at a word boundary, adding an ellipsis — for previews, table cells, and notification titles.

```js
import { stringTruncateSmart } from '@x-mori/string-truncate-smart';

stringTruncateSmart('The quick brown fox', 12); // 'The quick…'
```

## Install

This package is published to GitHub Packages. Add the `@x-mori` scope to your project's `.npmrc` once:

```ini
@x-mori:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}
```

The token needs the `read:packages` scope. Then install:

```sh
npm install @x-mori/string-truncate-smart
```

Requires Node.js 20 or newer. The package is an ES module with TypeScript types and no runtime dependencies.

## Usage

```js
stringTruncateSmart('The quick brown fox', 12, '...'); // 'The quick...'
stringTruncateSmart('Supercalifragilistic', 8);        // 'Superca…'  (no space fits, so it cuts mid-word)
stringTruncateSmart('Short', 20);                      // 'Short'     (fits, returned unchanged)
```

## API

### `stringTruncateSmart(text, maxLength, suffix?)`

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `string` | Text to shorten. |
| `maxLength` | `number` | Maximum length of the result, **including** the suffix. Nonnegative safe integer. |
| `suffix` | `string` | Appended after a cut. Default `'…'` (one character, U+2026). |

Returns the original text if `text.length <= maxLength`; otherwise a string of at most `maxLength` characters ending with `suffix`.

Throws:

- `TypeError` when `text` or `suffix` is not a string, or `maxLength` is not a nonnegative safe integer.
- `RangeError` when the text must be cut but `suffix` is longer than `maxLength`.

## Behavior

- Lengths are measured like `string.length`, in UTF-16 code units.
- The cut is placed at the last space that leaves room for the suffix, and trailing spaces before the suffix are removed. Only the space character counts as a word boundary; tabs and newlines do not.
- If no such space exists, the text is cut at exactly `maxLength - suffix.length`.
- A cut never splits a surrogate pair, so emoji and other characters outside the Basic Multilingual Plane are kept whole. The result may then be one character shorter than `maxLength`.

## Changes in 1.1.0

- Truncation no longer splits surrogate pairs, which could leave a broken character before the suffix (for example with emoji).
- A space immediately after the available budget now counts as a word boundary, so `('ab cd ef', 6)` gives `'ab cd…'` instead of `'ab…'`.

## License

MIT
