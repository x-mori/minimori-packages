/**
 * Outcome of {@link safeJsonParse}. Narrow on `error === null` to reach typed data.
 *
 * The failure error is typed as non-nullable so that narrowing works. It is the
 * thrown value, normally a SyntaxError; only a reviver that throws `null` or
 * `undefined` breaks this, and such a result is indistinguishable from success.
 */
export type ParseResult<T> = { data: T; error: null } | { data: null; error: NonNullable<unknown> };

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
 * @returns An object holding either the parsed data or the error. The type
 * parameter is not checked at runtime; validate data from untrusted sources.
 */
export function safeJsonParse<T = unknown>(text: string, reviver?: (this: unknown, key: string, value: unknown) => unknown): ParseResult<T>;
