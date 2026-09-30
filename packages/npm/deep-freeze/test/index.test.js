import test from 'node:test';
import assert from 'node:assert/strict';
import { deepFreeze } from '../src/index.js';

test('freezes nested objects and arrays in place', () => {
  const config = { db: { host: 'localhost' }, tags: ['a'], [Symbol('s')]: { x: 1 } };
  assert.equal(deepFreeze(config), config);
  assert.ok(Object.isFrozen(config) && Object.isFrozen(config.db) && Object.isFrozen(config.tags));
  assert.ok(Object.getOwnPropertySymbols(config).every(key => Object.isFrozen(config[key])));
});

test('handles cycles, primitives, functions, and non-empty typed arrays', () => {
  const node = { name: 'a' };
  node.self = node;
  assert.equal(deepFreeze(node).self, node);
  assert.equal(deepFreeze(5), 5);
  const handler = () => {};
  deepFreeze({ handler });
  assert.equal(Object.isFrozen(handler), false);
  const bytes = { data: new Uint8Array(2) };
  deepFreeze(bytes);
  assert.ok(Object.isFrozen(bytes) && !Object.isFrozen(bytes.data));
});
