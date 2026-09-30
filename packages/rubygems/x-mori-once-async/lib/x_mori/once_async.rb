# frozen_string_literal: true

module XMori
  # Runs a block at most once successfully and shares its result with every caller,
  # including callers on other threads that arrive while it is still running.
  #
  # - The first call runs the block. Concurrent callers wait for it instead of
  #   running the block again.
  # - After success, every later call returns the same object without running the block again.
  # - If the block raises, that caller receives the exception, the state resets,
  #   and the next caller (or one that was waiting) runs the block again.
  #
  # The block must not call the same instance's #call, or that thread waits forever.
  #
  # @example Lazily load a configuration file once
  #   require 'x_mori/once_async'
  #   CONFIG = XMori::OnceAsync.new { YAML.load_file('config.yml') }
  #   CONFIG.call # reads the file
  #   CONFIG.call # returns the same object
  class OnceAsync
    # Creates a one-time computation. The block does not run until #call.
    # @yieldreturn [Object] the value to share.
    # @raise [ArgumentError] if no block is given.
    def initialize(&block)
      raise ArgumentError, 'a block is required' unless block

      @block = block
      @mutex = Mutex.new
      @condition = ConditionVariable.new
      @state = :idle
    end

    # Returns the block's result, running it first if no call has succeeded yet.
    # @return [Object] the result of the first successful run.
    # @raise [Exception] whatever the block raised, for the caller whose run failed.
    def call
      @mutex.synchronize do
        @condition.wait(@mutex) while @state == :running
        return @value if @state == :done

        @state = :running
      end
      begin
        value = @block.call
      rescue Exception # rubocop:disable Lint/RescueException -- reset for any failure, then re-raise
        @mutex.synchronize { @state = :idle; @condition.broadcast }
        raise
      end
      @mutex.synchronize do
        @value = value
        @state = :done
        @block = nil # release anything the block captured
        @condition.broadcast
      end
      value
    end
  end
end
