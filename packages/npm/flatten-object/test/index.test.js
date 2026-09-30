import test from 'node:test';
import assert from 'node:assert/strict';
import { flattenObject } from '../src/index.js';

test('flattens nested plain objects to dot paths', () => {
  const tags = ['a'];
  const flat = flattenObject({ db: { host: 'localhost', port: 5432 }, tags, meta: {}, when: new Date(0) });
  assert.deepEqual(Object.keys(flat), ['db.host', 'db.port', 'tags', 'meta', 'when']);
  assert.equal(flat['db.port'], 5432);
  assert.equal(flat.tags, tags);
  assert.ok(flat.when instanceof Date);
});

test('rejects ambiguous or unsafe keys', () => {
  assert.throws(() => flattenObject({ 'a.b': 1 }), TypeError);
  assert.throws(() => flattenObject({ a: { '': 1 } }), TypeError);
  assert.throws(() => flattenObject(JSON.parse('{"a":{"__proto__":1}}')), TypeError);
  assert.throws(() => flattenObject([]), TypeError);
});
