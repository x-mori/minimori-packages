import test from 'node:test';
import assert from 'node:assert/strict';
import { removeEmptyValues } from '../src/index.js';

test('removes null and undefined but keeps other falsy values', () => {
  assert.deepEqual(removeEmptyValues({ name: 'Ada', email: null, phone: undefined, age: 0, ok: false, q: '' }), { name: 'Ada', age: 0, ok: false, q: '' });
});

test('optionally removes empty strings', () => {
  assert.deepEqual(removeEmptyValues({ a: null, b: '', c: 0 }, { emptyStrings: true }), { c: 0 });
});

test('filters only the top level', () => {
  const nested = { x: null };
  assert.equal(removeEmptyValues({ nested }).nested, nested);
});

test('rejects arrays and non-objects', () => {
  assert.throws(() => removeEmptyValues([]), TypeError);
  assert.throws(() => removeEmptyValues(null), TypeError);
});
