require 'minitest/autorun'
require 'stringio'
require 'x_mori/copy_to_clipboard_cli'

class PackageTest < Minitest::Test
  BIN = File.expand_path('../bin/xmori-copy', __dir__)

  def test_exposes_copy
    assert_respond_to XMori::CopyToClipboardCli, :copy
    refute_respond_to XMori::CopyToClipboardCli, :windows_payload
  end

  def test_executable_copies_standard_input
    copied = nil
    original_stdin = $stdin
    original_copy = XMori::CopyToClipboardCli.method(:copy)
    $stdin = StringIO.new("piped text\n")
    # Stub copy so the test never touches the real clipboard.
    replace_copy { |text| copied = text }
    load BIN
    assert_equal "piped text\n", copied
  ensure
    $stdin = original_stdin
    replace_copy(&original_copy)
  end

  def replace_copy(&implementation)
    XMori::CopyToClipboardCli.singleton_class.send(:remove_method, :copy)
    XMori::CopyToClipboardCli.define_singleton_method(:copy, &implementation)
  end

  def test_windows_payload_is_utf16le_with_bom
    payload = XMori::CopyToClipboardCli.send(:windows_payload, "hé \u{1F600}")
    assert_equal Encoding::UTF_16LE, payload.encoding
    assert_equal "﻿hé \u{1F600}".encode(Encoding::UTF_16LE), payload
    assert_equal "﻿a".encode(Encoding::UTF_16LE), XMori::CopyToClipboardCli.send(:windows_payload, 'a'.b)
  end
end
