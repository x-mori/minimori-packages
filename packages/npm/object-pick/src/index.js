const { hasOwn } = Object;
const isEnumerable = (object, key) => Object.prototype.propertyIsEnumerable.call(object, key);

/**
 * Copy selected own, enumerable properties into a new object.
 *
 * Keys that are missing, inherited, or non-enumerable are skipped, so
 * `objectPick(user, ['toString'])` returns an empty object. String and symbol keys
 * are both supported. The source is not changed, and values are copied by
 * reference (a shallow copy).
 *
 * The result has a null prototype, so a key such as `__proto__` becomes an
 * ordinary property instead of changing the prototype. Spread it
 * (`{ ...picked }`) if you need a normal object.
 *
 * @example
 * const user = { id: 7, name: 'Ada', password: 'hunter2' };
 * objectPick(user, ['id', 'name']); // { id: 7, name: 'Ada' }
 *
 * @param object - Source object.
 * @param keys - Keys to include, in the order they should appear.
 * @returns A new null-prototype object containing the requested properties.
 * @throws TypeError when object is not an object or keys is not an array.
 */
export function objectPick(object, keys) {
  if (object === null || typeof object !== 'object' || !Array.isArray(keys)) throw new TypeError('expected an object and keys');
  const result = Object.create(null);
  for (const key of keys) if (hasOwn(object, key) && isEnumerable(object, key)) result[key] = object[key];
  return result;
}
