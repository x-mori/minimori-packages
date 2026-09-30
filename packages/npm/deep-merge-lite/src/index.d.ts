/**
 * Merge two plain objects into a new object without changing either input.
 *
 * - Values from `right` win over values from `left`.
 * - When both sides hold a plain object under the same key, those objects are
 *   merged recursively.
 * - Arrays are not merged: an array from `right` replaces the one from `left`.
 *   Arrays are shallow-copied, so their items are shared with the inputs.
 * - Other values (class instances, Dates, Maps, functions) are copied by reference.
 * - The keys `__proto__`, `constructor`, and `prototype` are skipped at every
 *   depth, which prevents prototype pollution from untrusted JSON.
 *
 * Only own enumerable string keys are merged. Inputs must not contain cycles.
 *
 * @example
 * const defaults = { db: { host: 'localhost', port: 5432 }, tags: ['a'] };
 * const config = deepMergeLite(defaults, { db: { host: 'db.internal' }, tags: ['b'] });
 * // { db: { host: 'db.internal', port: 5432 }, tags: ['b'] }
 *
 * @param left - Base plain object.
 * @param right - Plain object whose values take precedence.
 * @returns A new object; it and every nested object it creates have a null prototype.
 * @throws TypeError when either argument is not a plain object.
 */
export function deepMergeLite<L extends Record<string, unknown>, R extends Record<string, unknown>>(left: L, right: R): L & R;
