# x-mori-once-async

Run an expensive block once, lazily, and share its result with every caller — including callers on other threads that arrive while it is still running. Failures are not cached, so the next call retries.

```ruby
require 'x_mori/once_async'

CONFIG = XMori::OnceAsync.new { YAML.load_file('config.yml') }

CONFIG.call # reads the file
CONFIG.call # returns the same object without reading again
```

## Install

Requires Ruby 3.1 or newer; no runtime dependencies.

The gem is published to GitHub Packages, which requires authentication even for public packages. Add it to your `Gemfile`:

```ruby
source 'https://rubygems.pkg.github.com/x-mori' do
  gem 'x-mori-once-async', '~> 1.1'
end
```

Then give Bundler a token with the `read:packages` scope, and install:

```sh
bundle config set --global https://rubygems.pkg.github.com/x-mori YOUR_GITHUB_USERNAME:YOUR_TOKEN
bundle install
```

## Behavior

| Situation | What happens |
| --- | --- |
| First call | Runs the block and stores its result. |
| Calls while the block runs | Wait (other threads block on a condition variable), then receive the same result. |
| Calls after success | Return the stored object; the block never runs again. |
| The block raises | That caller gets the exception. The state resets, so a waiting or later caller runs the block again. |

Unlike `||=`, concurrent first calls never run the block twice, and a `nil` or `false` result is cached like any other value.

The block must not call `#call` on the same instance, or that thread waits forever.

## API

### `XMori::OnceAsync.new { ... }`

Creates the one-time computation. The block does not run until `#call`. Raises `ArgumentError` when no block is given.

### `#call → Object`

Returns the result of the first successful run, running the block first if needed. Re-raises whatever the block raised, for the caller whose run failed.

## Changes in 1.1.0

- The block is released after it succeeds, so objects it captured can be garbage-collected.

## License

MIT
