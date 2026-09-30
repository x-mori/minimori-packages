import test from 'node:test';
import assert from 'node:assert/strict';
import { objectOmit } from '../src/index.js';

test('copies everything except the listed keys', () => {
  const secret = Symbol('secret');
  const source = { id: 7, name: 'Ada', password: 'x', [secret]: 1 };
  const result = objectOmit(source, ['password']);
  assert.deepEqual({ ...result }, { id: 7, name: 'Ada', [secret]: 1 });
  assert.equal(Object.hasOwn(source, 'password'), true);
});

test('skips non-enumerable properties and returns a null-prototype object', () => {
  const source = Object.defineProperty({ a: 1 }, 'hidden', { value: 2, enumerable: false });
  const result = objectOmit(source, []);
  assert.deepEqual(Reflect.ownKeys(result), ['a']);
  assert.equal(Object.getPrototypeOf(result), null);
});

test('rejects invalid arguments', () => {
  assert.throws(() => objectOmit(null, []), TypeError);
  assert.throws(() => objectOmit({}, 'a'), TypeError);
});
