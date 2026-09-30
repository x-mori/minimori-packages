import test from 'node:test';
import assert from 'node:assert/strict';
import { timeoutPromise } from '../src/index.js';

test('passes through values that settle in time', async () => {
  assert.equal(await timeoutPromise(Promise.resolve(4), 100), 4);
  assert.equal(await timeoutPromise(5, 100), 5);
  await assert.rejects(timeoutPromise(Promise.reject(new Error('own')), 100), { message: 'own' });
});

test('rejects with the message when the deadline passes', async () => {
  await assert.rejects(timeoutPromise(new Promise(() => {}), 1), /timed out/);
  await assert.rejects(timeoutPromise(new Promise(() => {}), 1, 'too slow'), { message: 'too slow' });
});

test('rejects deadlines the timer cannot represent', () => {
  assert.throws(() => timeoutPromise(1, -1), RangeError);
  assert.throws(() => timeoutPromise(1, 2_147_483_648), RangeError);
});
