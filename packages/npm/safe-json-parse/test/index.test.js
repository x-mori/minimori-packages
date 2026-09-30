import test from 'node:test';
import assert from 'node:assert/strict';
import { safeJsonParse } from '../src/index.js';

test('returns data for valid JSON', () => {
  assert.deepEqual(safeJsonParse('{"x":1}'), { data: { x: 1 }, error: null });
  assert.deepEqual(safeJsonParse('null'), { data: null, error: null });
});

test('returns the original error for invalid JSON', () => {
  const result = safeJsonParse('{');
  assert.equal(result.data, null);
  assert.ok(result.error instanceof SyntaxError);
  assert.ok(safeJsonParse(undefined).error instanceof SyntaxError);
});

test('applies the reviver and captures reviver errors', () => {
  assert.deepEqual(safeJsonParse('{"n":2}', (key, value) => key === 'n' ? value * 2 : value).data, { n: 4 });
  const boom = new Error('boom');
  assert.equal(safeJsonParse('1', () => { throw boom; }).error, boom);
});
