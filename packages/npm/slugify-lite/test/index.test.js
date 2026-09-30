import test from 'node:test';
import assert from 'node:assert/strict';
import { slugifyLite } from '../src/index.js';

test('creates lowercase ASCII slugs', () => {
  assert.equal(slugifyLite(' Café & Tea! '), 'cafe-tea');
  assert.equal(slugifyLite('  Hello, World!  '), 'hello-world');
  assert.equal(slugifyLite('Crème Brûlée & Café'), 'creme-brulee-cafe');
  assert.equal(slugifyLite('--a--b--'), 'a-b');
});

test('returns an empty string when nothing ASCII remains', () => {
  assert.equal(slugifyLite('こんにちは'), '');
  assert.equal(slugifyLite(''), '');
});

test('shared regular expressions give the same result on repeated calls', () => {
  assert.equal(slugifyLite('A B'), 'a-b');
  assert.equal(slugifyLite('A B'), 'a-b');
});

test('rejects non-strings', () => {
  assert.throws(() => slugifyLite(null), TypeError);
});
