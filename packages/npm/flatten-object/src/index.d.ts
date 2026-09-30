/**
 * Turn nested plain objects into a single-level object keyed by dot paths.
 *
 * Only plain objects (object literals and null-prototype objects) are
 * descended into. Arrays, empty objects, class instances, Dates, and primitives
 * are kept as leaf values. Use `unflattenObject` from `@x-mori/unflatten-object`
 * to reverse the result.
 *
 * To keep paths unambiguous and safe to expand again, an empty key, a key that
 * contains a dot, and the keys `__proto__`, `constructor`, and `prototype` are
 * rejected. Inputs must not contain cycles.
 *
 * @example
 * flattenObject({ db: { host: 'localhost', port: 5432 }, tags: ['a'], meta: {} });
 * // { 'db.host': 'localhost', 'db.port': 5432, tags: ['a'], meta: {} }
 *
 * @param object - Plain object to flatten.
 * @returns A new null-prototype object keyed by dot paths.
 * @throws TypeError when object is not a plain object or contains an unsafe key.
 */
export function flattenObject(object: Record<string, unknown>): Record<string, unknown>;
