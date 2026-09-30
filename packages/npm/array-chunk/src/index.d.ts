/**
 * Split an array into consecutive chunks of at most `size` items.
 *
 * Every chunk except possibly the last has exactly `size` items. Items keep
 * their original order, each chunk is a new array, and the input is not changed.
 *
 * @example
 * arrayChunk([1, 2, 3, 4, 5], 2); // [[1, 2], [3, 4], [5]]
 * arrayChunk([], 3);              // []
 *
 * // Send rows to an API in batches of 100.
 * for (const batch of arrayChunk(rows, 100)) await api.insert(batch);
 *
 * @param items - Array to split.
 * @param size - Maximum chunk length; a positive safe integer.
 * @returns An array of chunks. An empty input gives an empty array.
 * @throws TypeError when items is not an array or size is not a positive safe integer.
 */
export function arrayChunk<T>(items: readonly T[], size: number): T[][];
