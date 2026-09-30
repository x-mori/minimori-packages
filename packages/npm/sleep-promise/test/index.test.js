import test from 'node:test';
import assert from 'node:assert/strict';
import { sleepPromise } from '../src/index.js';

test('resolves after the delay', async () => {
  assert.equal(await sleepPromise(0), undefined);
});

test('rejects with the abort reason', async () => {
  const controller = new AbortController();
  controller.abort(new Error('stop'));
  await assert.rejects(sleepPromise(1, { signal: controller.signal }), { message: 'stop' });

  const later = new AbortController();
  const waiting = sleepPromise(10_000, { signal: later.signal });
  later.abort();
  await assert.rejects(waiting, { name: 'AbortError' });
});

test('rejects delays the timer cannot represent', () => {
  assert.throws(() => sleepPromise(-1), RangeError);
  assert.throws(() => sleepPromise(Infinity), RangeError);
  assert.throws(() => sleepPromise(2_147_483_648), RangeError);
});
