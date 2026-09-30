# x-mori-port-check

Check whether a local TCP port is free, and find the next free port in a range — for development servers, test setups, and CLI tools.

```ruby
require 'x_mori/port_check'

XMori::PortCheck.available?(3000)    # => true or false
XMori::PortCheck.next_free(3000)     # => first free port from 3000 through 3099, or nil
```

## Install

Requires Ruby 3.1 or newer; no runtime dependencies.

The gem is published to GitHub Packages, which requires authentication even for public packages. Add it to your `Gemfile`:

```ruby
source 'https://rubygems.pkg.github.com/x-mori' do
  gem 'x-mori-port-check', '~> 1.1'
end
```

Then give Bundler a token with the `read:packages` scope, and install:

```sh
bundle config set --global https://rubygems.pkg.github.com/x-mori YOUR_GITHUB_USERNAME:YOUR_TOKEN
bundle install
```

## Usage

```ruby
port = XMori::PortCheck.next_free(8080, limit: 20) or abort 'No free port from 8080 through 8099'
puts "Starting on http://localhost:#{port}"
```

A check is only a snapshot: another process can take the port before you use it. When you control the server, bind port `0` and read the port the operating system assigned instead:

```ruby
server = TCPServer.new('127.0.0.1', 0)
port = server.addr[1]
```

## API

### `XMori::PortCheck.available?(port, host: '127.0.0.1') → Boolean`

Tries to open a TCP listener on `host:port` and closes it immediately.

- Returns `false` when the port is in use (`EADDRINUSE`) or binding is not permitted (`EACCES`, for example a port below 1024 without privileges).
- Returns `true` for port `0`, which asks the operating system for any free port.
- `host` is the local address to test. The default checks the loopback interface only; pass `'0.0.0.0'` to check all IPv4 interfaces, which is what most servers bind.

Raises `ArgumentError` when `port` is not an Integer from 0 through 65535, and other `SystemCallError`s from binding, for example `EADDRNOTAVAIL` when `host` is not an address of this machine.

### `XMori::PortCheck.next_free(start, host: '127.0.0.1', limit: 100) → Integer or nil`

Tests ports `start`, `start + 1`, … and returns the first available one. At most `limit` ports are tested, and the search stops at 65535. Returns `nil` when none is free.

Raises `ArgumentError` when `start` is not an Integer from 1 through 65535 or `limit` is not a positive Integer.

## License

MIT
