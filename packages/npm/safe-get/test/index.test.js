import test from 'node:test';
import assert from 'node:assert/strict';
import { safeGet } from '../src/index.js';

test('reads nested values and returns the fallback only for missing paths', () => {
  const config = { db: { port: 0, hosts: ['a', 'b'], unset: undefined } };
  assert.equal(safeGet(config, 'db.port', 5432), 0);
  assert.equal(safeGet(config, 'db.hosts.1'), 'b');
  assert.equal(safeGet(config, 'cache.ttl', 60), 60);
  assert.equal(safeGet(config, 'db.unset', 1), undefined);
  assert.equal(safeGet(null, 'a', 9), 9);
});

test('supports segment arrays, symbols, and string properties', () => {
  const key = Symbol('k');
  assert.equal(safeGet({ 'a.b': 1 }, ['a.b']), 1);
  assert.equal(safeGet({ [key]: 2 }, [key]), 2);
  assert.equal(safeGet('abc', 'length'), 3);
});

test('never follows the prototype chain', () => {
  assert.equal(safeGet({}, 'constructor', 'none'), 'none');
  assert.equal(safeGet({}, '__proto__', 'none'), 'none');
});

test('rejects invalid paths', () => {
  assert.throws(() => safeGet({}, 'a..b'), TypeError);
  assert.throws(() => safeGet({}, 1), TypeError);
});
