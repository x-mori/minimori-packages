import test from 'node:test';
import assert from 'node:assert/strict';
import { retryAsync } from '../src/index.js';

const flush = () => new Promise(resolve => setImmediate(resolve));

test('retries until the operation succeeds', async () => {
  const seen = [];
  const value = await retryAsync(attempt => { seen.push(attempt); if (attempt < 3) throw new Error('wait'); return 7; });
  assert.equal(value, 7);
  assert.deepEqual(seen, [1, 2, 3]);
});

test('rethrows the final error unchanged', async () => {
  const failure = new Error('last');
  let calls = 0;
  await assert.rejects(retryAsync(() => { calls++; throw failure; }, { attempts: 2 }), error => error === failure);
  assert.equal(calls, 2);
});

test('waits with exponential backoff', async (t) => {
  t.mock.timers.enable({ apis: ['setTimeout'] });
  let calls = 0;
  const done = retryAsync(() => { if (++calls < 3) throw new Error('wait'); return 'ok'; }, { attempts: 3, delay: 100, factor: 3 });
  await flush();
  assert.equal(calls, 1);
  t.mock.timers.tick(100);
  await flush();
  assert.equal(calls, 2);
  t.mock.timers.tick(299);
  await flush();
  assert.equal(calls, 2);
  t.mock.timers.tick(1);
  assert.equal(await done, 'ok');
});

test('caps huge waits instead of letting the timer overflow', async (t) => {
  t.mock.timers.enable({ apis: ['setTimeout'] });
  let calls = 0;
  const done = retryAsync(() => { calls++; throw new Error('x'); }, { attempts: 2, delay: 1e12 });
  await flush();
  t.mock.timers.tick(1_000);
  await flush();
  assert.equal(calls, 1);
  t.mock.timers.tick(2_147_483_647);
  await assert.rejects(done, { message: 'x' });
  assert.equal(calls, 2);
});

test('rejects invalid options', async () => {
  await assert.rejects(retryAsync(() => 1, { attempts: 0 }), TypeError);
  await assert.rejects(retryAsync(() => 1, { factor: 0.5 }), TypeError);
  await assert.rejects(retryAsync(null), TypeError);
});
