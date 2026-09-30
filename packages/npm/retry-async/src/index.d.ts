/** Options for {@link retryAsync}. */
export interface RetryOptions {
  /** Maximum number of calls, including the first. Positive integer; default 3. */
  attempts?: number;
  /** Wait in milliseconds after the first failure. Nonnegative; default 0. */
  delay?: number;
  /** Multiplier applied to each later wait. At least 1; default 2. */
  factor?: number;
}

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
 * @returns A promise for the first successful result.
 * @throws The last operation error, or a TypeError for invalid options.
 */
export function retryAsync<T>(operation: (attempt: number) => PromiseLike<T> | T, options?: RetryOptions): Promise<T>;
