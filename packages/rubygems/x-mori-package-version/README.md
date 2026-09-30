# x-mori-package-version

Look up the installed version of a Ruby gem as a string, without loading the gem.

```ruby
require 'x_mori/package_version'

XMori::PackageVersion.of('rake') # => "13.2.1"
```

## Install

Requires Ruby 3.1 or newer; no runtime dependencies.

The gem is published to GitHub Packages, which requires authentication even for public packages. Add it to your `Gemfile`:

```ruby
source 'https://rubygems.pkg.github.com/x-mori' do
  gem 'x-mori-package-version', '~> 1.1'
end
```

Then give Bundler a token with the `read:packages` scope, and install:

```sh
bundle config set --global https://rubygems.pkg.github.com/x-mori YOUR_GITHUB_USERNAME:YOUR_TOKEN
bundle install
```

## Usage

Report dependency versions in a health or diagnostics endpoint:

```ruby
versions = %w[rails puma sidekiq].to_h do |name|
  [name, XMori::PackageVersion.of(name)]
rescue Gem::LoadError
  [name, nil]
end
```

Compare versions with `Gem::Version`, not as strings:

```ruby
Gem::Version.new(XMori::PackageVersion.of('rack')) >= Gem::Version.new('3.0')
```

## API

### `XMori::PackageVersion.of(name) → String`

Returns the version of the installed gem `name`, such as `"7.1.3"`. The name is exact and case-sensitive.

- Inside `bundle exec`, or after `Bundler.setup`, the version Bundler resolved is returned.
- Otherwise, when several versions are installed, the highest one is returned.
- The gem is looked up through `Gem::Specification.find_by_name` and is not required or loaded.

Raises:

- `ArgumentError` when `name` is not a non-empty `String`.
- `Gem::MissingSpecError`, a subclass of `Gem::LoadError`, when the gem is not installed.

## Changes in 1.1.0

- A non-`String` name, such as a Symbol, raises `ArgumentError` instead of `NoMethodError`.

## License

MIT
