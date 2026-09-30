# x-mori-console-prefix

Write log lines prefixed with a UTC timestamp and a namespace, safely from multiple threads.

```ruby
require 'x_mori/console_prefix'

log = XMori::ConsolePrefix.new('worker')
log.log('job started')
# [2026-01-15T09:30:00Z] [worker] job started
```

## Install

Requires Ruby 3.1 or newer; no runtime dependencies.

The gem is published to GitHub Packages, which requires authentication even for public packages. Add it to your `Gemfile`:

```ruby
source 'https://rubygems.pkg.github.com/x-mori' do
  gem 'x-mori-console-prefix', '~> 1.1'
end
```

Then give Bundler a token with the `read:packages` scope, and install:

```sh
bundle config set --global https://rubygems.pkg.github.com/x-mori YOUR_GITHUB_USERNAME:YOUR_TOKEN
bundle install
```

## Usage

```ruby
errors = XMori::ConsolePrefix.new('billing', io: $stderr)
errors.log("charge failed: #{error.message}")

# Log to a file
File.open('app.log', 'a') do |file|
  XMori::ConsolePrefix.new('import', io: file).log('done')
end
```

Inject a clock for predictable output in tests:

```ruby
io = StringIO.new
log = XMori::ConsolePrefix.new('test', io: io, clock: -> { Time.utc(2026, 1, 1) })
log.log('hello') # => "[2026-01-01T00:00:00Z] [test] hello"
```

## API

### `XMori::ConsolePrefix.new(namespace, io: $stdout, clock: -> { Time.now.utc })`

| Argument | Description |
| --- | --- |
| `namespace` | Label shown in brackets on every line, such as a component name. |
| `io:` | Destination; any object with `puts`, such as `$stderr`, a `File`, or a `StringIO`. |
| `clock:` | Callable returning the `Time` for each line. Defaults to the current UTC time. |

### `#log(message) → String`

Writes `[timestamp] [namespace] message` followed by a newline, and returns the line without the newline.

- The timestamp is ISO 8601 with second precision, from `Time#iso8601`.
- The message is converted with `to_s`. A message containing newlines is written as-is, so only its first line carries the prefix.
- Each line is written while holding a mutex, so lines from different threads never interleave. One instance can be shared between threads.

## Changes in 1.1.0

- The `] [namespace] ` part of the prefix is built once per logger instead of on every line.

## License

MIT
