// setTimeout stores delays as a signed 32-bit integer; larger values fire after 1 ms.
const MAX_DELAY = 2_147_483_647;

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
export function timeoutPromise(value, ms, message = 'Operation timed out') {
  if (!Number.isFinite(ms) || ms < 0 || ms > MAX_DELAY) throw new RangeError(`ms must be a finite number from 0 through ${MAX_DELAY}`);
  let timer;
  const deadline = new Promise((_, reject) => { timer = setTimeout(() => reject(new Error(message)), ms); });
  return Promise.race([value, deadline]).finally(() => clearTimeout(timer));
}
