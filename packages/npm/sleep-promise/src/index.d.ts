/** Options for {@link sleepPromise}. */
export interface SleepOptions {
  /** Cancels the wait. The promise rejects with `signal.reason`, or an `AbortError` DOMException. */
  signal?: AbortSignal;
}

/**
 * Resolve after a nonnegative number of milliseconds.
 *
 * Pass an AbortSignal to cancel the wait: the promise rejects with
 * `signal.reason` (or an `AbortError` DOMException) and the timer is cleared, so
 * an aborted sleep never keeps the process alive. A signal that is already
 * aborted rejects without starting a timer.
 *
 * @example
 * await sleepPromise(250);
 *
 * const controller = new AbortController();
 * setTimeout(() => controller.abort(), 100);
 * await sleepPromise(5_000, { signal: controller.signal }); // rejects after ~100 ms
 *
 * @param ms - Delay in milliseconds, from 0 through 2147483647 (about 24.8 days).
 * @param options - Optional settings.
 * @returns A promise that resolves with undefined when the delay elapses.
 * @throws RangeError when ms is negative, not finite, or above 2147483647.
 */
export function sleepPromise(ms: number, options?: SleepOptions): Promise<void>;
