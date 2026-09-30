const COMBINING_MARKS = /[̀-ͯ]/g;
const NON_SLUG_RUNS = /[^a-z0-9]+/g;
const EDGE_HYPHENS = /^-|-$/g;

/**
 * Convert text to a lowercase ASCII slug for URLs and file names.
 *
 * Accented Latin letters are reduced to their base letter (é → e) using
 * Unicode NFKD normalization. Every run of other characters, including
 * spaces, punctuation, and non-Latin scripts, becomes one hyphen, and hyphens
 * at either end are removed.
 *
 * Text with no ASCII letters or digits (for example, only Japanese or emoji)
 * produces an empty string, so provide a fallback such as an ID in that case.
 *
 * @example
 * slugifyLite('  Hello, World!  ');   // 'hello-world'
 * slugifyLite('Crème Brûlée & Café'); // 'creme-brulee-cafe'
 * slugifyLite('こんにちは') || 'post-42'; // 'post-42'
 *
 * @param text - Text to convert.
 * @returns A slug containing only a-z, 0-9, and single hyphens; possibly empty.
 * @throws TypeError when text is not a string.
 */
export function slugifyLite(text) {
  if (typeof text !== 'string') throw new TypeError('text must be a string');
  return text.normalize('NFKD').replace(COMBINING_MARKS, '').toLowerCase().replace(NON_SLUG_RUNS, '-').replace(EDGE_HYPHENS, '');
}
