/**
 * Copy an object without its null and undefined values.
 *
 * Only the top level is filtered; nested objects and arrays are kept as-is and
 * shared with the input. Set `emptyStrings` to also drop `''`. Other falsy
 * values such as `0`, `false`, and `NaN` are always kept. Only own enumerable
 * string keys are copied, and the input is not changed.
 *
 * @example
 * removeEmptyValues({ name: 'Ada', email: null, phone: undefined, age: 0 });
 * // { name: 'Ada', age: 0 }
 *
 * removeEmptyValues({ q: '', page: 2 }, { emptyStrings: true });
 * // { page: 2 }
 *
 * @param object - Non-array object to filter.
 * @param options - Filtering options.
 * @param options.emptyStrings - Also remove empty strings; default false.
 * @returns A new plain object with the remaining properties.
 * @throws TypeError when object is null, an array, or not an object.
 */
export function removeEmptyValues(object, { emptyStrings = false } = {}) {
  if (object === null || typeof object !== 'object' || Array.isArray(object)) throw new TypeError('expected an object');
  // fromEntries defines own properties, so a key such as __proto__ stays data.
  return Object.fromEntries(Object.entries(object).filter(([, value]) => value != null && !(emptyStrings && value === '')));
}
