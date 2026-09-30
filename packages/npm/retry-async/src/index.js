// setTimeout stores delays as a signed 32-bit integer; larger values fire after 1 ms.
const MAX_DELAY = 2_147_483_647;

/**
 * Call an operation until it succeeds or the attempt limit is reached.
 *
 * The operation receives its one-based attempt number and may return a value or
 * a promise. After failure number n, the wait is `delay * factor ** (n - 1)`
 * milliseconds, capped at 2147483647 ms. No wait follows the final attempt: its
 * error is rethrown unchanged.
 *
 * @example
 * const user = await retryAsync(
 *   attempt => fetchUser(id, { attempt }),
 *   { attempts: 4, delay: 200, factor: 2 }, // waits 200, 400, 800 ms between tries
 * );
 *
 * @param operation - Function to invoke on each attempt.
 * @param options - Retry settings.
 * @param options.attempts - Maximum number of calls, including the first. Positive integer; default 3.
 * @param options.delay - Wait in milliseconds after the first failure. Nonnegative; default 0.
 * @param options.factor - Multiplier applied to each later wait. At least 1; default 2.
 * @returns A promise for the first successful result.
 * @throws The last operation error, or a TypeError for invalid options.
 */
export async function retryAsync(operation, { attempts = 3, delay = 0, factor = 2 } = {}) {
  if (typeof operation !== 'function' || !Number.isInteger(attempts) || attempts < 1 || !Number.isFinite(delay) || delay < 0 || !Number.isFinite(factor) || factor < 1) throw new TypeError('invalid retry options');
  let wait = Math.min(delay, MAX_DELAY);
  for (let attempt = 1; ; attempt++) {
    try { return await operation(attempt); }
    catch (error) {
      if (attempt >= attempts) throw error;
      if (wait > 0) await new Promise(resolve => setTimeout(resolve, wait));
      wait = Math.min(wait * factor, MAX_DELAY);
    }
  }
}
