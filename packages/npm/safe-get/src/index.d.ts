/**
 * Read a nested own property without throwing when a parent is missing.
 *
 * The path is a dot-separated string or an array of segments. Use an array when
 * a key contains a dot or is a symbol. Only own properties are followed, so a
 * path such as `'constructor'` or `'__proto__'` never reaches the prototype
 * chain. Array indexes and string properties like `length` work normally.
 *
 * The fallback is returned only when a segment is absent or a parent is null
 * or undefined. A property that exists with the value `undefined` returns
 * `undefined`, not the fallback.
 *
 * @example
 * const config = { db: { port: 0, hosts: ['a', 'b'] } };
 * safeGet(config, 'db.port', 5432);        // 0 (present, so no fallback)
 * safeGet(config, 'db.hosts.1');           // 'b'
 * safeGet(config, 'cache.ttl', 60);        // 60
 * safeGet({ 'a.b': 1 }, ['a.b']);          // 1
 *
 * @param object - Value to read from; may be null or a primitive.
 * @param path - Dot path, or an array of property keys.
 * @param fallback - Value returned when the path is missing.
 * @returns The value at the path, or the fallback. The type parameter is not
 * checked at runtime.
 * @throws TypeError when path is not a string or array, or contains an empty segment.
 */
export function safeGet<T = unknown>(object: unknown, path: string | readonly PropertyKey[], fallback?: T): T;
