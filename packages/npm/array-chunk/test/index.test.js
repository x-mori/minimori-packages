import test from 'node:test';
import assert from 'node:assert/strict';
import { arrayChunk } from '../src/index.js';

test('splits into fixed-size chunks with a shorter final chunk', () => {
  assert.deepEqual(arrayChunk([1, 2, 3, 4, 5], 2), [[1, 2], [3, 4], [5]]);
  assert.deepEqual(arrayChunk([1, 2, 3], 3), [[1, 2, 3]]);
  assert.deepEqual(arrayChunk([1, 2], 5), [[1, 2]]);
  assert.deepEqual(arrayChunk([], 3), []);
});

test('rejects invalid arguments', () => {
  assert.throws(() => arrayChunk([], 0), TypeError);
  assert.throws(() => arrayChunk([], 1.5), TypeError);
  assert.throws(() => arrayChunk('abc', 1), TypeError);
});
