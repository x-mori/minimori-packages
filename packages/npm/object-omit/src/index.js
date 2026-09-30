const isEnumerable = (object, key) => Object.prototype.propertyIsEnumerable.call(object, key);

/**
 * Copy own, enumerable properties except the selected keys.
 *
 * String and symbol keys are both copied. Inherited and non-enumerable
 * properties are never copied. The source is not changed, and values are copied
 * by reference (a shallow copy).
 *
 * The result has a null prototype, so a key such as `__proto__` becomes an
 * ordinary property instead of changing the prototype. Spread it
 * (`{ ...rest }`) if you need a normal object.
 *
 * @example
 * const user = { id: 7, name: 'Ada', password: 'hunter2' };
 * objectOmit(user, ['password']); // { id: 7, name: 'Ada' }
 *
 * @param object - Source object.
 * @param keys - Keys to exclude. Keys that are not present are ignored.
 * @returns A new null-prototype object containing the remaining properties.
 * @throws TypeError when object is not an object or keys is not an array.
 */
export function objectOmit(object, keys) {
  if (object === null || typeof object !== 'object' || !Array.isArray(keys)) throw new TypeError('expected an object and keys');
  const excluded = new Set(keys);
  const result = Object.create(null);
  for (const key of Reflect.ownKeys(object)) if (!excluded.has(key) && isEnumerable(object, key)) result[key] = object[key];
  return result;
}
