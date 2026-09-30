/**
 * Reject if a promise does not settle before a deadline.
 *
 * The result settles the same way as `value` when it settles first. Otherwise it
 * rejects with `new Error(message)`. The timer is cleared as soon as either side
 * settles, so a fast operation does not keep the process alive.
 *
 * This wrapper cannot stop the underlying work. When cancellation matters, give
 * the operation its own AbortSignal as well, for example
 * `fetch(url, { signal: AbortSignal.timeout(ms) })`.
 *
 * @example
 * const response = await timeoutPromise(fetch(url), 5_000, 'Request took longer than 5 s');
 *
 * @param value - Promise, thenable, or plain value to await.
 * @param ms - Deadline in milliseconds, from 0 through 2147483647 (about 24.8 days).
 * @param message - Error message used when the deadline expires.
 * @returns A promise for the settled value of `value`.
 * @throws RangeError synchronously when ms is negative, not finite, or above 2147483647.
 */
export function timeoutPromise<T>(value: PromiseLike<T> | T, ms: number, message?: string): Promise<Awaited<T>>;
