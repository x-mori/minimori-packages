require 'minitest/autorun'
require 'x_mori/once_async'

class PackageTest < Minitest::Test
  def test_runs_the_block_once
    calls = 0
    once = XMori::OnceAsync.new { calls += 1; 7 }
    assert_equal 7, once.call
    assert_equal 7, once.call
    assert_equal 1, calls
  end

  def test_concurrent_callers_share_one_run
    calls = 0
    once = XMori::OnceAsync.new { calls += 1; sleep 0.05; Object.new }
    results = 10.times.map { Thread.new { once.call } }.map(&:value)
    assert_equal 1, calls
    assert_equal 1, results.uniq(&:object_id).size
  end

  def test_failure_resets_so_a_later_call_retries
    attempts = 0
    once = XMori::OnceAsync.new { attempts += 1; raise 'boom' if attempts == 1; :ok }
    assert_raises(RuntimeError) { once.call }
    assert_equal :ok, once.call
    assert_equal :ok, once.call
    assert_equal 2, attempts
  end

  def test_requires_a_block
    assert_raises(ArgumentError) { XMori::OnceAsync.new }
  end
end
