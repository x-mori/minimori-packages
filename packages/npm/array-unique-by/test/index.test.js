import test from 'node:test';
import assert from 'node:assert/strict';
import { arrayUniqueBy } from '../src/index.js';

test('keeps the first item for each property key', () => {
  const users = [{ id: 1, name: 'Ada' }, { id: 2, name: 'Lin' }, { id: 1, name: 'Ada L.' }];
  assert.deepEqual(arrayUniqueBy(users, 'id'), [users[0], users[1]]);
});

test('accepts a selector callback with the index', () => {
  assert.deepEqual(arrayUniqueBy(['a', 'B', 'b'], s => s.toLowerCase()), ['a', 'B']);
  assert.deepEqual(arrayUniqueBy([5, 6, 7], (_, index) => index % 2), [5, 6]);
});

test('uses SameValueZero and skips holes', () => {
  assert.deepEqual(arrayUniqueBy([NaN, NaN, 0, -0], x => x), [NaN, 0]);
  assert.deepEqual(arrayUniqueBy([1, , 2], x => x), [1, 2]);
});

test('rejects invalid arguments', () => {
  assert.throws(() => arrayUniqueBy(null, 'id'), TypeError);
  assert.throws(() => arrayUniqueBy([], 1), TypeError);
});
