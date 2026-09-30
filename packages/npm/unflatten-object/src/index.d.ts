/**
 * Expand dot-path keys into nested objects.
 *
 * Each key is split on dots and every segment becomes one level of nesting.
 * Values are placed as-is: an object supplied as a value is never merged into
 * or mutated. This reverses `flattenObject` from `@x-mori/flatten-object`.
 *
 * The input is rejected, without partial output, when a path has an empty
 * segment (`'a..b'`, `'.a'`), uses `__proto__`, `constructor`, or `prototype`,
 * or when two paths conflict (`'a'` and `'a.b'`, in either order).
 *
 * @example
 * unflattenObject({ 'db.host': 'localhost', 'db.port': 5432, debug: true });
 * // { db: { host: 'localhost', port: 5432 }, debug: true }
 *
 * @param flat - Object whose own enumerable string keys are dot paths.
 * @returns A new nested object; every container it creates has a null prototype.
 * @throws TypeError when flat is not a non-array object, a path is unsafe, or paths conflict.
 */
export function unflattenObject(flat: Record<string, unknown>): Record<string, unknown>;
