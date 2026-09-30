/**
 * Group items into a Map by a selected key.
 *
 * The selector is either a property name or a callback that receives
 * `(item, index)`. Map keys keep their original type, so `1` and `'1'` form
 * separate groups, and objects or symbols can be keys. Keys are compared with
 * SameValueZero (the rule `Map` uses). Groups appear in the order their key was
 * first seen, and items keep their input order within each group.
 *
 * @example
 * const orders = [{ id: 1, status: 'paid' }, { id: 2, status: 'open' }, { id: 3, status: 'paid' }];
 * const byStatus = groupByKey(orders, 'status');
 * byStatus.get('paid'); // [{ id: 1, ... }, { id: 3, ... }]
 *
 * groupByKey([1.2, 1.8, 2.5], n => Math.floor(n)); // Map { 1 => [1.2, 1.8], 2 => [2.5] }
 *
 * @param items - Items to group.
 * @param selector - Property name, or a callback returning the key for an item.
 * @returns A Map from each key to the array of items with that key.
 * @throws TypeError when items is not an array or selector is neither a string nor a function.
 */
export function groupByKey<T, P extends keyof T>(items: readonly T[], selector: P): Map<T[P], T[]>;
export function groupByKey<T, K>(items: readonly T[], selector: (item: T, index: number) => K): Map<K, T[]>;
