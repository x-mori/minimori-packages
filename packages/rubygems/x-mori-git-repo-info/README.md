# x-mori-git-repo-info

Extract the GitHub owner and repository name from a Git remote URL, in any of the formats Git uses.

```ruby
require 'x_mori/git_repo_info'

XMori::GitRepoInfo.parse('git@github.com:x-mori/minimori-packages.git')
# => { owner: "x-mori", repo: "minimori-packages" }
```

## Install

Requires Ruby 3.1 or newer; no runtime dependencies.

The gem is published to GitHub Packages, which requires authentication even for public packages. Add it to your `Gemfile`:

```ruby
source 'https://rubygems.pkg.github.com/x-mori' do
  gem 'x-mori-git-repo-info', '~> 1.1'
end
```

Then give Bundler a token with the `read:packages` scope, and install:

```sh
bundle config set --global https://rubygems.pkg.github.com/x-mori YOUR_GITHUB_USERNAME:YOUR_TOKEN
bundle install
```

## Usage

Build links from the current repository's remote:

```ruby
remote = `git remote get-url origin`.strip
info = XMori::GitRepoInfo.parse(remote)
puts "https://github.com/#{info[:owner]}/#{info[:repo]}/actions"
```

## API

### `XMori::GitRepoInfo.parse(remote) → Hash`

Returns a new hash with `:owner` and `:repo` keys.

Accepted forms, each with or without a trailing `.git` and a trailing `/`:

| Form | Example |
| --- | --- |
| HTTPS | `https://github.com/owner/repo` |
| SSH URL | `ssh://git@github.com/owner/repo.git` |
| scp-style SSH | `git@github.com:owner/repo.git` |
| Git protocol | `git://github.com/owner/repo.git` |

- The host and scheme are matched case-insensitively; owner and repository names keep their case.
- Repository names may contain letters, digits, `.`, `_`, and `-`, so `my.site.git` gives `my.site`.

Raises `ArgumentError` when `remote` is not a `String`, is not on `github.com` (GitHub Enterprise Server hosts are rejected), uses another scheme such as plain `http`, or does not have exactly an owner and a repository in its path.

## Changes in 1.1.0

- Regular expressions are compiled once as constants, and the host is compared without allocating a lowercase copy. Results are unchanged.

## License

MIT
