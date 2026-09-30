# frozen_string_literal: true

require 'time'

module XMori
  # Writes log lines prefixed with a UTC timestamp and a namespace, such as
  # <tt>[2026-01-15T09:30:00Z] [worker] job started</tt>.
  #
  # One instance can be shared between threads: each line is written while holding
  # a mutex, so lines from different threads never interleave.
  #
  # @example
  #   require 'x_mori/console_prefix'
  #   log = XMori::ConsolePrefix.new('worker')
  #   log.log('job started')        # prints [2026-01-15T09:30:00Z] [worker] job started
  #
  #   errors = XMori::ConsolePrefix.new('worker', io: $stderr)
  #   errors.log("failed: #{error.message}")
  class ConsolePrefix
    # Creates a logger for one namespace.
    # @param namespace [#to_s] label shown in square brackets on every line, such as a component name.
    # @param io [#puts] output destination; defaults to standard output. Any object with +puts+ works, such as a File or StringIO.
    # @param clock [#call] callable returning the Time for each line; defaults to the current UTC time.
    #   Useful for deterministic tests.
    def initialize(namespace, io: $stdout, clock: -> { Time.now.utc })
      @prefix = "] [#{namespace}] "
      @io = io
      @clock = clock
      @mutex = Mutex.new
    end

    # Writes one line and returns it.
    #
    # The timestamp is ISO 8601 with second precision. A message that contains
    # newlines is written as-is, so only its first line carries the prefix.
    # @param message [#to_s] text to write after the prefix.
    # @return [String] the line that was written, without the trailing newline.
    def log(message)
      line = "[#{@clock.call.iso8601}#{@prefix}#{message}"
      @mutex.synchronize { @io.puts(line) }
      line
    end
  end
end
