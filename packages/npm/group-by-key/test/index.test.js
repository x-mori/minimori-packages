import test from 'node:test';
import assert from 'node:assert/strict';
import { groupByKey } from '../src/index.js';

test('groups by property name in first-seen order', () => {
  const orders = [{ id: 1, status: 'paid' }, { id: 2, status: 'open' }, { id: 3, status: 'paid' }];
  const groups = groupByKey(orders, 'status');
  assert.deepEqual([...groups.keys()], ['paid', 'open']);
  assert.deepEqual(groups.get('paid').map(order => order.id), [1, 3]);
});

test('groups by callback and keeps key types distinct', () => {
  assert.deepEqual([...groupByKey([1.2, 1.8, 2.5], n => Math.floor(n))], [[1, [1.2, 1.8]], [2, [2.5]]]);
  assert.equal(groupByKey([{ k: 1 }, { k: '1' }], 'k').size, 2);
});

test('skips holes in sparse arrays', () => {
  assert.deepEqual([...groupByKey([1, , 1], x => x)], [[1, [1, 1]]]);
});

test('rejects invalid arguments', () => {
  assert.throws(() => groupByKey({}, 'x'), TypeError);
  assert.throws(() => groupByKey([], null), TypeError);
});
