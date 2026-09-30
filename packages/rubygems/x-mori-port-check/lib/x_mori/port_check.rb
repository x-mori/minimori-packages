# frozen_string_literal: true

require 'socket'

module XMori
  # Checks whether local TCP ports can be bound, and finds a free one.
  #
  # @example
  #   require 'x_mori/port_check'
  #   XMori::PortCheck.available?(3000)          # => true or false
  #   port = XMori::PortCheck.next_free(3000)    # first free port from 3000 through 3099, or nil
  #   XMori::PortCheck.next_free(8080, host: '0.0.0.0', limit: 10)
  module PortCheck
    module_function

    # Tries to bind a TCP listener on the port, then closes it immediately.
    #
    # A port is reported unavailable when binding fails because it is in use
    # (+EADDRINUSE+) or not permitted (+EACCES+, for example a port below 1024
    # without privileges). Other errors, such as a host address that does not
    # belong to this machine, are raised.
    #
    # The result is only a snapshot: another process can take the port before you
    # use it. When possible, bind port 0 yourself and read the assigned port instead.
    #
    # @param port [Integer] port from 0 through 65535; 0 asks the OS for any free port, so it is normally available.
    # @param host [String] local address to bind; <tt>'127.0.0.1'</tt> by default. Use <tt>'0.0.0.0'</tt> to check all IPv4 interfaces.
    # @return [Boolean] +true+ if the port could be bound.
    # @raise [ArgumentError] if port is not an Integer from 0 through 65535.
    # @raise [SystemCallError] for bind errors other than +EADDRINUSE+ and +EACCES+.
    def available?(port, host: '127.0.0.1')
      raise ArgumentError, 'port must be 0..65535' unless port.is_a?(Integer) && port.between?(0, 65_535)

      TCPServer.new(host, port).close
      true
    rescue Errno::EADDRINUSE, Errno::EACCES
      false
    end

    # Finds the first port that can be bound, testing upward from +start+.
    #
    # At most +limit+ ports are tested, and the search stops at 65535. The same
    # race as #available? applies to the returned port.
    #
    # @param start [Integer] first port to test, from 1 through 65535.
    # @param host [String] local address to bind; <tt>'127.0.0.1'</tt> by default.
    # @param limit [Integer] positive maximum number of ports to test; 100 by default.
    # @return [Integer, nil] the first available port, or +nil+ if none in the range is free.
    # @raise [ArgumentError] if start or limit is invalid.
    def next_free(start, host: '127.0.0.1', limit: 100)
      raise ArgumentError, 'invalid port range' unless start.is_a?(Integer) && start.between?(1, 65_535) && limit.is_a?(Integer) && limit.positive?

      (start..[start + limit - 1, 65_535].min).find { |port| available?(port, host: host) }
    end
  end
end
