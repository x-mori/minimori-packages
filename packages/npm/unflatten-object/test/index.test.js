import test from 'node:test';
import assert from 'node:assert/strict';
import { unflattenObject } from '../src/index.js';

test('expands dot paths into nested objects', () => {
  const result = unflattenObject({ 'db.host': 'localhost', 'db.port': 5432, debug: true });
  assert.equal(result.db.host, 'localhost');
  assert.equal(result.db.port, 5432);
  assert.equal(result.debug, true);
});

test('rejects unsafe or empty segments', () => {
  assert.throws(() => unflattenObject({ '__proto__.x': 1 }), TypeError);
  assert.throws(() => unflattenObject({ 'a..b': 1 }), TypeError);
  assert.equal(({}).x, undefined);
});

test('rejects conflicting paths without mutating supplied values', () => {
  const leaf = { x: 1 };
  assert.throws(() => unflattenObject({ a: leaf, 'a.b': 2 }), TypeError);
  assert.throws(() => unflattenObject({ 'a.b': 2, a: 1 }), TypeError);
  assert.deepEqual(leaf, { x: 1 });
});
