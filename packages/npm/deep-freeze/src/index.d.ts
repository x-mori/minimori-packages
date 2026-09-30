/**
 * Freeze an object and every object reachable from it, in place.
 *
 * Walks own string and symbol properties (enumerable or not) of objects and
 * arrays, then freezes each one with `Object.freeze`. Shared references and
 * cycles are visited once. Primitives and functions are returned unchanged,
 * and functions are not descended into.
 *
 * Limits of JavaScript freezing apply: a non-empty typed array or DataView
 * cannot be frozen and is left writable, and the internal contents of Map, Set,
 * and Date cannot be frozen. Accessor properties are read once while walking.
 *
 * @example
 * const config = deepFreeze({ db: { host: 'localhost' }, tags: ['a'] });
 * config.db.host = 'x'; // ignored, or a TypeError in strict mode
 * Object.isFrozen(config.tags); // true
 *
 * @param value - Value to freeze.
 * @returns The same value, now deeply frozen. The type marks only the top level
 * readonly; nested objects are frozen at runtime as well.
 */
export function deepFreeze<T>(value: T): Readonly<T>;
