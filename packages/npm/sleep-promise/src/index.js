// setTimeout stores delays as a signed 32-bit integer; larger values fire after 1 ms.
const MAX_DELAY = 2_147_483_647;

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
 * @param options.signal - AbortSignal that cancels the wait.
 * @returns A promise that resolves with undefined when the delay elapses.
 * @throws RangeError when ms is negative, not finite, or above 2147483647.
 */
export function sleepPromise(ms, { signal } = {}) {
  if (!Number.isFinite(ms) || ms < 0 || ms > MAX_DELAY) throw new RangeError(`ms must be a finite number from 0 through ${MAX_DELAY}`);
  if (signal?.aborted) return Promise.reject(abortReason(signal));
  return new Promise((resolve, reject) => {
    const timer = setTimeout(() => { signal?.removeEventListener('abort', abort); resolve(); }, ms);
    function abort() { clearTimeout(timer); reject(abortReason(signal)); }
    signal?.addEventListener('abort', abort, { once: true });
  });
}

function abortReason(signal) {
  return signal.reason ?? new DOMException('This operation was aborted', 'AbortError');
}
