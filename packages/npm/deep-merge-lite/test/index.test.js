import test from 'node:test';
import assert from 'node:assert/strict';
import { deepMergeLite } from '../src/index.js';

const plain = value => JSON.parse(JSON.stringify(value));

test('merges nested plain objects and lets right win', () => {
  const defaults = { db: { host: 'localhost', port: 5432 }, tags: ['a'] };
  const merged = deepMergeLite(defaults, { db: { host: 'db.internal' }, tags: ['b'] });
  assert.deepEqual(plain(merged), { db: { host: 'db.internal', port: 5432 }, tags: ['b'] });
});

test('does not mutate or share nested containers with inputs', () => {
  const left = { a: { b: { c: 1 } }, list: [1] };
  const right = { a: { b: { d: 2 } } };
  const merged = deepMergeLite(left, right);
  merged.a.b.c = 99;
  merged.list.push(2);
  assert.deepEqual(left, { a: { b: { c: 1 } }, list: [1] });
  assert.deepEqual(right, { a: { b: { d: 2 } } });
  assert.deepEqual(plain(merged), { a: { b: { c: 99, d: 2 } }, list: [1, 2] });
});

test('ignores prototype-polluting keys at every depth', () => {
  const merged = deepMergeLite({}, JSON.parse('{"__proto__":{"polluted":true},"x":{"constructor":{"y":1}}}'));
  assert.equal(({}).polluted, undefined);
  assert.equal(Object.hasOwn(merged, '__proto__'), false);
  assert.equal(Object.hasOwn(merged.x, 'constructor'), false);
});

test('rejects non-plain objects', () => {
  assert.throws(() => deepMergeLite([], {}), TypeError);
  assert.throws(() => deepMergeLite({}, new Date()), TypeError);
});
