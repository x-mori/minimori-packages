import test from 'node:test';
import assert from 'node:assert/strict';
import { stringTruncateSmart } from '../src/index.js';

test('cuts at a word boundary and appends the suffix', () => {
  assert.equal(stringTruncateSmart('hello world', 8), 'hello…');
  assert.equal(stringTruncateSmart('The quick brown fox', 12), 'The quick…');
  assert.equal(stringTruncateSmart('The quick brown fox', 12, '...'), 'The quick...');
  assert.equal(stringTruncateSmart('hello world', 6), 'hello…');
  assert.equal(stringTruncateSmart('ab cd ef', 6), 'ab cd…');
});

test('falls back to an exact cut and leaves short text alone', () => {
  assert.equal(stringTruncateSmart('Supercalifragilistic', 8), 'Superca…');
  assert.equal(stringTruncateSmart('abc', 3), 'abc');
});

test('never splits a surrogate pair', () => {
  const emoji = '\u{1F600}';
  const result = stringTruncateSmart(`ab${emoji}cdef`, 4);
  assert.equal(result, 'ab…');
  assert.equal(stringTruncateSmart(`ab${emoji}cdef`, 5), `ab${emoji}…`);
});

test('rejects invalid arguments', () => {
  assert.throws(() => stringTruncateSmart('abcdef', 2, '...'), RangeError);
  assert.throws(() => stringTruncateSmart('abc', -1), TypeError);
});
