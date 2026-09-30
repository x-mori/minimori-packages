/** Options for {@link removeEmptyValues}. */
export interface RemoveEmptyValuesOptions {
  /** Also remove empty strings. Default false. */
  emptyStrings?: boolean;
}

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
 * @returns A new plain object with the remaining properties.
 * @throws TypeError when object is null, an array, or not an object.
 */
export function removeEmptyValues<T extends Record<string, unknown>>(object: T, options?: RemoveEmptyValuesOptions): Partial<T>;
