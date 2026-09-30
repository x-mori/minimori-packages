/**
 * Shorten text to a maximum length, including the suffix.
 *
 * Text that already fits is returned unchanged. Otherwise the cut is made before
 * the last space that leaves room for the suffix, and trailing spaces are
 * removed. If no space fits, the text is cut at the exact budget. A cut never
 * splits a surrogate pair, so emoji and other astral characters stay intact.
 *
 * @example
 * stringTruncateSmart('The quick brown fox', 12);        // 'The quick…'
 * stringTruncateSmart('Supercalifragilistic', 8);        // 'Superca…'
 * stringTruncateSmart('The quick brown fox', 12, '...'); // 'The quick...'
 *
 * @param text - Text to shorten.
 * @param maxLength - Maximum length of the result in UTF-16 code units (`string.length`).
 * @param suffix - Text appended after a cut; defaults to the single character '…'.
 * @returns The original text if it fits, otherwise the shortened text plus suffix.
 * @throws TypeError for a non-string text or suffix, or a maxLength that is not a nonnegative safe integer.
 * @throws RangeError when text must be cut and the suffix is longer than maxLength.
 */
export function stringTruncateSmart(text, maxLength, suffix = '…') {
  if (typeof text !== 'string' || !Number.isSafeInteger(maxLength) || maxLength < 0 || typeof suffix !== 'string') throw new TypeError('invalid truncate arguments');
  if (text.length <= maxLength) return text;
  if (suffix.length > maxLength) throw new RangeError('suffix is longer than maxLength');
  let budget = maxLength - suffix.length;
  // Keep a high surrogate together with its low surrogate.
  if (budget > 0 && isHighSurrogate(text.charCodeAt(budget - 1))) budget--;
  const space = text.lastIndexOf(' ', budget);
  const cut = space > 0 ? text.slice(0, space).trimEnd() : text.slice(0, budget);
  return cut + suffix;
}

function isHighSurrogate(code) {
  return code >= 0xd800 && code <= 0xdbff;
}
