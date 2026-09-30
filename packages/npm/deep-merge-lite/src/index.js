const isRecord = value => value !== null && typeof value === 'object' && (Object.getPrototypeOf(value) === Object.prototype || Object.getPrototypeOf(value) === null);
const blocked = new Set(['__proto__', 'constructor', 'prototype']);

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
export function deepMergeLite(left, right) {
  if (!isRecord(left) || !isRecord(right)) throw new TypeError('expected plain objects');
  return mergeInto(cloneRecord(left), right);
}

// Every target passed here is a fresh null-prototype clone, so mutating it is safe.
function mergeInto(target, source) {
  for (const [key, value] of Object.entries(source)) {
    if (blocked.has(key)) continue;
    const existing = target[key];
    target[key] = isRecord(value) && isRecord(existing) ? mergeInto(existing, value) : copy(value);
  }
  return target;
}

function cloneRecord(source) {
  const result = Object.create(null);
  for (const [key, value] of Object.entries(source)) if (!blocked.has(key)) result[key] = copy(value);
  return result;
}

function copy(value) {
  return isRecord(value) ? cloneRecord(value) : Array.isArray(value) ? value.slice() : value;
}
