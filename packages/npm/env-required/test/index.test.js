import test from 'node:test';
import assert from 'node:assert/strict';
import { envRequired } from '../src/index.js';

test('returns exactly the requested values', () => {
  assert.deepEqual(envRequired(['A', 'B'], { A: 'a', B: 'b', C: 'c' }), { A: 'a', B: 'b' });
  assert.deepEqual(envRequired('PORT', { PORT: '8080' }), { PORT: '8080' });
});

test('lists every missing or empty name in one error', () => {
  assert.throws(() => envRequired(['A', 'B', 'C'], { B: '', C: 'ok' }), { message: 'Missing required environment variables: A, B' });
  assert.throws(() => envRequired('B', {}), { message: 'Missing required environment variable: B' });
});

test('does not read inherited properties', () => {
  assert.throws(() => envRequired('toString', {}), /toString/);
});

test('rejects invalid names', () => {
  assert.throws(() => envRequired('', {}), TypeError);
  assert.throws(() => envRequired([1], {}), TypeError);
});
