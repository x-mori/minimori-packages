require 'minitest/autorun'
require 'socket'
require 'x_mori/port_check'

class PackageTest < Minitest::Test
  def test_reports_free_and_used_ports
    assert XMori::PortCheck.available?(0)
    server = TCPServer.new('127.0.0.1', 0)
    port = server.addr[1]
    refute XMori::PortCheck.available?(port)
  ensure
    server&.close
  end

  def test_next_free_skips_used_ports
    server = TCPServer.new('127.0.0.1', 0)
    port = server.addr[1]
    found = XMori::PortCheck.next_free(port, limit: 20)
    assert found.nil? || found > port
  ensure
    server&.close
  end

  def test_rejects_invalid_arguments
    assert_raises(ArgumentError) { XMori::PortCheck.available?(65_536) }
    assert_raises(ArgumentError) { XMori::PortCheck.available?('80') }
    assert_raises(ArgumentError) { XMori::PortCheck.next_free(0) }
    assert_raises(ArgumentError) { XMori::PortCheck.next_free(3000, limit: 0) }
  end
end
