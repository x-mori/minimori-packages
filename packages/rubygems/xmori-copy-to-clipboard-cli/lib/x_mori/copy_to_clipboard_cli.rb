# frozen_string_literal: true

module XMori
  # Copies text to the system clipboard by piping it to the platform's clipboard command.
  #
  # @example From Ruby
  #   require 'x_mori/copy_to_clipboard_cli'
  #   XMori::CopyToClipboardCli.copy("Hello from Ruby")
  #
  # @example From a shell, with the bundled executable
  #   git rev-parse HEAD | xmori-copy
  module CopyToClipboardCli
    # Clipboard commands to try, in order, for each platform family.
    COMMANDS = {
      macos: [%w[pbcopy]],
      windows: [%w[clip]],
      other: [%w[wl-copy], %w[xclip -selection clipboard], %w[xsel --clipboard --input]]
    }.freeze

    module_function

    # Copies text to the clipboard with the first clipboard command that succeeds.
    #
    # Commands are tried in this order:
    # - macOS: +pbcopy+
    # - Windows: +clip+ (the text is sent as UTF-16LE so non-ASCII characters survive)
    # - Linux and other Unix systems: +wl-copy+ (Wayland), then +xclip+, then +xsel+ (X11)
    #
    # A command that is not installed, or that exits with a failure status, is skipped.
    # Text is copied exactly, including any trailing newline.
    #
    # @param text [String, #to_s] text to copy; other objects are converted with +to_s+.
    # @return [true] after a clipboard command succeeds.
    # @raise [RuntimeError] if no clipboard command is available or every command fails.
    #   On Linux, install +wl-clipboard+, +xclip+, or +xsel+.
    def copy(text)
      text = text.to_s
      COMMANDS.fetch(platform).each do |command|
        payload = command.first == 'clip' ? windows_payload(text) : text
        IO.popen(command, 'wb') { |pipe| pipe.write(payload) }
        return true if $?.success?
      rescue SystemCallError
        next
      end
      raise RuntimeError, 'no working clipboard command found'
    end

    # @return [Symbol] the platform family used to choose clipboard commands.
    def platform
      case RUBY_PLATFORM
      when /darwin/ then :macos
      when /mswin|mingw|cygwin/ then :windows
      else :other
      end
    end

    # clip.exe reads its input in the console code page unless it starts with a UTF-16LE byte order mark.
    # @return [String] text encoded as UTF-16LE with a byte order mark.
    def windows_payload(text)
      utf8 = text.encoding == Encoding::BINARY ? text.dup.force_encoding(Encoding::UTF_8) : text
      "﻿#{utf8}".encode(Encoding::UTF_16LE, invalid: :replace, undef: :replace)
    end
    private_class_method :platform, :windows_payload
  end
end
