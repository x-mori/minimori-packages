const blocked = new Set(['__proto__', 'constructor', 'prototype']);

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
export function unflattenObject(flat) {
  if (flat === null || typeof flat !== 'object' || Array.isArray(flat)) throw new TypeError('expected a flat object');
  const result = Object.create(null);
  const containers = new WeakSet([result]);
  for (const [path, value] of Object.entries(flat)) {
    const parts = path.split('.');
    for (const part of parts) if (!part || blocked.has(part)) throw new TypeError(`unsafe dot path: ${JSON.stringify(path)}`);
    let current = result;
    const last = parts.length - 1;
    for (let i = 0; i < last; i++) {
      const part = parts[i];
      if (!Object.hasOwn(current, part)) { current[part] = Object.create(null); containers.add(current[part]); }
      else if (!containers.has(current[part])) throw new TypeError(`conflicting dot paths at ${JSON.stringify(path)}`);
      current = current[part];
    }
    if (Object.hasOwn(current, parts[last])) throw new TypeError(`duplicate or conflicting dot paths at ${JSON.stringify(path)}`);
    current[parts[last]] = value;
  }
  return result;
}
