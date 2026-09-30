# x-mori-redact-secrets

Hide passwords, tokens, API keys, and Bearer credentials in nested hashes, arrays, and strings before they are logged or sent to an error tracker.

```ruby
require 'x_mori/redact_secrets'

XMori::RedactSecrets.call(
  { user: 'ada', password: 'hunter2', headers: { 'Authorization' => 'Bearer abc' }, note: 'retry with Bearer xyz' }
)
# => { user: "ada", password: "[REDACTED]", headers: { "Authorization" => "[REDACTED]" }, note: "retry with Bearer [REDACTED]" }
```

## Install

Requires Ruby 3.1 or newer; no runtime dependencies.

The gem is published to GitHub Packages, which requires authentication even for public packages. Add it to your `Gemfile`:

```ruby
source 'https://rubygems.pkg.github.com/x-mori' do
  gem 'x-mori-redact-secrets', '~> 1.1'
end
```

Then give Bundler a token with the `read:packages` scope, and install:

```sh
bundle config set --global https://rubygems.pkg.github.com/x-mori YOUR_GITHUB_USERNAME:YOUR_TOKEN
bundle install
```

## Usage

```ruby
logger.info(XMori::RedactSecrets.call(params.to_unsafe_h))    # Rails request parameters
Sentry.set_context('payload', XMori::RedactSecrets.call(payload))
```

## API

### `XMori::RedactSecrets.call(value) → Object`

Returns a sanitized copy of `value`. The input is never modified.

| Value | Result |
| --- | --- |
| `Hash` | A new hash with the same keys. A value whose key matches `PATTERN` becomes `"[REDACTED]"`; other values are sanitized recursively. |
| `Array` | A new array with every element sanitized. |
| `String` | Every `Bearer <token>` becomes `Bearer [REDACTED]`, ignoring case. |
| anything else | Returned as-is, including Structs, custom objects, and numbers. |

A hash or array that contains itself is replaced by `"[Circular]"` where it repeats. The same object appearing twice without a cycle is sanitized both times. Hash subclasses and default values are not preserved in the copy.

### `XMori::RedactSecrets::PATTERN`

```ruby
/(?:password|passwd|token|secret|api[_-]?key|authorization)/i
```

Keys are converted with `to_s` and matched anywhere, ignoring case, so `password`, `db_password`, `access_token`, `refreshToken`, `client_secret`, `apiKey`, `api-key`, and `Authorization` are all redacted.

## Limitations

Redaction is pattern-based, so treat it as a safety net, not a guarantee. Secrets under unexpected keys (such as `pin` or `ssn`), inside free text other than Bearer tokens, or inside custom objects are not detected. Avoid logging sensitive payloads in the first place where you can.

## Changes in 1.1.0

- Cycle detection uses an identity hash instead of `object_id` keys, and the Bearer pattern is compiled once.

## License

MIT
