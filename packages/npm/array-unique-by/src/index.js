/**
 * Remove items whose selected key was already seen, keeping the first one.
 *
 * The selector is either a property name or a callback that receives
 * `(item, index)`. Keys are compared with SameValueZero (the rule `Set` uses),
 * so `NaN` matches `NaN`, but two different objects with equal contents are
 * different keys. The input array is not changed.
 *
 * @example
 * const users = [{ id: 1, name: 'Ada' }, { id: 2, name: 'Lin' }, { id: 1, name: 'Ada L.' }];
 * arrayUniqueBy(users, 'id');                     // [{ id: 1, name: 'Ada' }, { id: 2, name: 'Lin' }]
 * arrayUniqueBy(['a', 'B', 'b'], s => s.toLowerCase()); // ['a', 'B']
 *
 * @param items - Items to deduplicate.
 * @param selector - Property name, or a callback returning the key for an item.
 * @returns A new array with the first item for each key, in input order.
 * @throws TypeError when items is not an array or selector is neither a string nor a function.
 */
export function arrayUniqueBy(items, selector) {
  if (!Array.isArray(items) || (typeof selector !== 'function' && typeof selector !== 'string')) throw new TypeError('expected an array and a selector');
  const select = typeof selector === 'function' ? selector : item => item?.[selector];
  const seen = new Set();
  const result = [];
  for (let index = 0; index < items.length; index++) {
    if (!(index in items)) continue; // skip holes, as Array.prototype.filter does
    const item = items[index];
    const key = select(item, index);
    if (!seen.has(key)) { seen.add(key); result.push(item); }
  }
  return result;
}
