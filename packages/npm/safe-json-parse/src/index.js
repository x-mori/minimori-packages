/**
 * Parse JSON and return the outcome instead of throwing.
 *
 * On success the result is `{ data, error: null }`. On failure it is
 * `{ data: null, error }`, where error is the original exception (normally a
 * SyntaxError). Check `error`, not `data`, because the valid JSON text `null`
 * also produces `data: null`.
 *
 * Non-string input is converted the way `JSON.parse` converts it, so
 * `undefined` produces a SyntaxError result. An exception thrown by the reviver
 * is also returned as the error.
 *
 * @example
 * const { data, error } = safeJsonParse(body);
 * if (error) return res.status(400).send('Invalid JSON');
 * use(data);
 *
 * @param text - JSON text to parse.
 * @param reviver - Optional `JSON.parse` reviver.
 * @returns An object holding either the parsed data or the error.
 */
export function safeJsonParse(text, reviver) {
  try { return { data: JSON.parse(text, reviver), error: null }; }
  catch (error) { return { data: null, error }; }
}
