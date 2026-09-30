# xmori-copy-to-clipboard-cli

Copy text to the system clipboard on macOS, Windows, and Linux — from Ruby, or from a shell pipe with the bundled `xmori-copy` command.

```sh
git rev-parse HEAD | xmori-copy
```

```ruby
require 'x_mori/copy_to_clipboard_cli'

XMori::CopyToClipboardCli.copy('Hello from Ruby')
```

## Install

Requires Ruby 3.1 or newer; no runtime dependencies. On Linux, one of `wl-clipboard` (Wayland), `xclip`, or `xsel` (X11) must be installed.

The gem is published to GitHub Packages, which requires authentication even for public packages. Add it to your `Gemfile`:

```ruby
source 'https://rubygems.pkg.github.com/x-mori' do
  gem 'xmori-copy-to-clipboard-cli', '~> 1.1'
end
```

Then give Bundler a token with the `read:packages` scope, and install:

```sh
bundle config set --global https://rubygems.pkg.github.com/x-mori YOUR_GITHUB_USERNAME:YOUR_TOKEN
bundle install
```

Run the command with `bundle exec xmori-copy`, or install the gem globally to put `xmori-copy` on your `PATH`.

## Command line

`xmori-copy` copies everything it reads from standard input:

```sh
cat ~/.ssh/id_ed25519.pub | xmori-copy
echo -n "no trailing newline" | xmori-copy
```

It exits with status 0 on success. If no clipboard command works, it prints `xmori-copy: no working clipboard command found` to standard error and exits with status 1.

## API

### `XMori::CopyToClipboardCli.copy(text) → true`

Copies `text` (converted with `to_s`) exactly, including any trailing newline, using the first command that succeeds:

| Platform | Commands tried, in order |
| --- | --- |
| macOS | `pbcopy` |
| Windows | `clip` (text is sent as UTF-16 so accented letters, CJK, and emoji survive) |
| Linux and other Unix | `wl-copy`, `xclip -selection clipboard`, `xsel --clipboard --input` |

A command that is not installed, cannot start, or exits with a failure status is skipped.

Raises `RuntimeError` when no command succeeds.

## Changes in 1.1.0

- **Fixed:** the `xmori-copy` executable failed with `LoadError` in 1.0.1 because it required a file that does not exist in this gem. It now loads the library and reports errors cleanly.
- On Windows, non-ASCII text is sent to `clip` as UTF-16 with a byte order mark. Previously it was garbled by the console code page.
- A command that exists but cannot be started (for example, permission denied) is skipped instead of raising.

## License

MIT
