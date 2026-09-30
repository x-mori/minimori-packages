require 'minitest/autorun'
require 'x_mori/redact_secrets'

class PackageTest < Minitest::Test
  def test_redacts_sensitive_keys_and_bearer_tokens
    assert_equal '[REDACTED]', XMori::RedactSecrets.call({ 'api_key' => 'abc' })['api_key']
    assert_equal 'Bearer [REDACTED]', XMori::RedactSecrets.call('Bearer abc')
    input = { user: 'ada', password: 'hunter2', headers: { 'Authorization' => 'Bearer abc' }, note: 'sent bearer xyz ok', list: [{ accessToken: 't' }] }
    expected = { user: 'ada', password: '[REDACTED]', headers: { 'Authorization' => '[REDACTED]' }, note: 'sent Bearer [REDACTED] ok', list: [{ accessToken: '[REDACTED]' }] }
    assert_equal expected, XMori::RedactSecrets.call(input)
  end

  def test_does_not_modify_the_input
    input = { password: 'x', nested: ['Bearer y'] }
    XMori::RedactSecrets.call(input)
    assert_equal({ password: 'x', nested: ['Bearer y'] }, input)
  end

  def test_marks_cycles_but_not_shared_references
    loop_hash = { name: 'a' }
    loop_hash[:self] = loop_hash
    assert_equal({ name: 'a', self: '[Circular]' }, XMori::RedactSecrets.call(loop_hash))
    shared = { v: 1 }
    assert_equal [{ v: 1 }, { v: 1 }], XMori::RedactSecrets.call([shared, shared])
  end

  def test_passes_other_values_through
    object = Object.new
    assert_same object, XMori::RedactSecrets.call(object)
    assert_equal 5, XMori::RedactSecrets.call(5)
  end
end
