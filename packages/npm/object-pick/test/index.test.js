import test from 'node:test';
import assert from 'node:assert/strict';
import { objectPick } from '../src/index.js';

test('copies only the listed own properties', () => {
  const result = objectPick({ id: 7, name: 'Ada', password: 'x' }, ['id', 'name', 'missing']);
  assert.deepEqual({ ...result }, { id: 7, name: 'Ada' });
});

test('skips inherited and non-enumerable properties', () => {
  assert.equal(Object.hasOwn(objectPick({}, ['toString']), 'toString'), false);
  const source = Object.defineProperty({}, 'hidden', { value: 1, enumerable: false });
  assert.deepEqual(Reflect.ownKeys(objectPick(source, ['hidden'])), []);
});

test('treats __proto__ as ordinary data', () => {
  const result = objectPick(JSON.parse('{"__proto__":{"polluted":true}}'), ['__proto__']);
  assert.equal(Object.getPrototypeOf(result), null);
  assert.deepEqual(result.__proto__, { polluted: true });
  assert.equal(({}).polluted, undefined);
});

test('rejects invalid arguments', () => {
  assert.throws(() => objectPick(1, []), TypeError);
  assert.throws(() => objectPick({}, null), TypeError);
});
