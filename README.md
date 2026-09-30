# Developer utility packages

Small, dependency-free developer utilities for four ecosystems, each published separately to GitHub Packages. Every package has its own README with installation steps, examples, and a full API reference. Follow the links below.

> **Arrived here from a package page?** GitHub Packages shows this repository README on Maven, NuGet, and RubyGems package pages instead of the package's own README. Find your package in the index below to open its documentation.

| Registry | Packages | Runtime | Current version |
| --- | ---: | --- | --- |
| npm | 18 | Node.js 20+ | 1.1.0 |
| Apache Maven | 18 | Java 17+ | 1.1.0 |
| NuGet | 9 | .NET 8 | 1.1.0 |
| RubyGems | 7 | Ruby 3.1+ | 1.1.0 |

## Package index

### npm — `@x-mori/*`

| Package | What it does |
| --- | --- |
| [`@x-mori/array-chunk`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/array-chunk#readme) | Split an array into chunks of a fixed size. |
| [`@x-mori/array-unique-by`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/array-unique-by#readme) | Remove duplicates by a property or computed key. |
| [`@x-mori/deep-freeze`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/deep-freeze#readme) | Recursively freeze an object in place. |
| [`@x-mori/deep-merge-lite`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/deep-merge-lite#readme) | Deep-merge two plain objects without mutation or prototype pollution. |
| [`@x-mori/env-required`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/env-required#readme) | Read required environment variables and report every missing one. |
| [`@x-mori/flatten-object`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/flatten-object#readme) | Turn nested objects into dot-path keys. |
| [`@x-mori/group-by-key`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/group-by-key#readme) | Group array items into a `Map` by key. |
| [`@x-mori/object-omit`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/object-omit#readme) | Copy an object without some keys. |
| [`@x-mori/object-pick`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/object-pick#readme) | Copy only selected keys of an object. |
| [`@x-mori/remove-empty-values`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/remove-empty-values#readme) | Drop `null`, `undefined`, and optionally `''` values. |
| [`@x-mori/retry-async`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/retry-async#readme) | Retry an async operation with exponential backoff. |
| [`@x-mori/safe-get`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/safe-get#readme) | Read a nested property by path with a fallback. |
| [`@x-mori/safe-json-parse`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/safe-json-parse#readme) | Parse JSON into `{ data, error }` without throwing. |
| [`@x-mori/sleep-promise`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/sleep-promise#readme) | Awaitable, cancellable delay. |
| [`@x-mori/slugify-lite`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/slugify-lite#readme) | Convert text to a URL slug. |
| [`@x-mori/string-truncate-smart`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/string-truncate-smart#readme) | Shorten text at a word boundary with an ellipsis. |
| [`@x-mori/timeout-promise`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/timeout-promise#readme) | Reject a promise that takes too long. |
| [`@x-mori/unflatten-object`](https://github.com/x-mori/minimori-packages/tree/main/packages/npm/unflatten-object#readme) | Expand dot-path keys into nested objects. |

### Maven — `io.github.xmori:*`

| Artifact | What it does |
| --- | --- |
| [`age-from-date`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/age-from-date#readme) | Age in completed years from a birth date. |
| [`business-days`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/business-days#readme) | Count weekdays in a date range. |
| [`date-range`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/date-range#readme) | Generate dates by day, week, or month. |
| [`email-normalizer`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/email-normalizer#readme) | Trim an email address and lowercase its domain. |
| [`human-duration`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/human-duration#readme) | Format milliseconds as `1d 2h 5m`. |
| [`human-file-size`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/human-file-size#readme) | Format bytes as `1.5 MB` or `1.5 MiB`. |
| [`initials-from-name`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/initials-from-name#readme) | Initials from a person's name. |
| [`join-url`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/join-url#readme) | Append path segments to a URL with single slashes. |
| [`mask-string`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/mask-string#readme) | Hide all but the last characters of a string. |
| [`otp-generator-lite`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/otp-generator-lite#readme) | RFC 6238 TOTP codes for authenticator apps. |
| [`password-strength-lite`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/password-strength-lite#readme) | Simple password score and advice. |
| [`query-string-object`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/query-string-object#readme) | Parse and build query strings with repeated keys. |
| [`random-id-lite`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/random-id-lite#readme) | Random, readable IDs without look-alike characters. |
| [`random-string-secure`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/random-string-secure#readme) | Random strings from a custom alphabet. |
| [`relative-time-lite`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/relative-time-lite#readme) | `5 minutes ago` / `in 2 days` text. |
| [`secure-pin`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/secure-pin#readme) | Random numeric PINs and verification codes. |
| [`strip-tracking-params`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/strip-tracking-params#readme) | Remove `utm_*`, `fbclid`, and similar parameters. |
| [`url-normalizer-lite`](https://github.com/x-mori/minimori-packages/tree/main/packages/maven/url-normalizer-lite#readme) | Normalize HTTP URLs for comparison. |

### NuGet — `XMori.*`

| Package | What it does |
| --- | --- |
| [`XMori.ApiErrorNormalizer`](https://github.com/x-mori/minimori-packages/tree/main/packages/nuget/api-error-normalizer#readme) | Convert HTTP and other exceptions to one error record. |
| [`XMori.AsyncQueueLite`](https://github.com/x-mori/minimori-packages/tree/main/packages/nuget/async-queue-lite#readme) | Limit how many async operations run at once. |
| [`XMori.DebouncePromise`](https://github.com/x-mori/minimori-packages/tree/main/packages/nuget/debounce-promise#readme) | Debounce async calls and share the result. |
| [`XMori.FetchJsonSafe`](https://github.com/x-mori/minimori-packages/tree/main/packages/nuget/fetch-json-safe#readme) | JSON GET that returns a result instead of throwing. |
| [`XMori.FetchRetry`](https://github.com/x-mori/minimori-packages/tree/main/packages/nuget/fetch-retry#readme) | Retry GET requests on 429, 5xx, and network errors. |
| [`XMori.FetchTimeout`](https://github.com/x-mori/minimori-packages/tree/main/packages/nuget/fetch-timeout#readme) | Per-request deadline for GET requests. |
| [`XMori.IsPrivateIp`](https://github.com/x-mori/minimori-packages/tree/main/packages/nuget/is-private-ip#readme) | Detect private, loopback, and link-local IP addresses. |
| [`XMori.MemoizeAsync`](https://github.com/x-mori/minimori-packages/tree/main/packages/nuget/memoize-async#readme) | Cache async results by key for a fixed time. |
| [`XMori.RateLimitMemory`](https://github.com/x-mori/minimori-packages/tree/main/packages/nuget/rate-limit-memory#readme) | In-memory fixed-window rate limiter per key. |

### RubyGems

| Gem | What it does |
| --- | --- |
| [`x-mori-console-prefix`](https://github.com/x-mori/minimori-packages/tree/main/packages/rubygems/x-mori-console-prefix#readme) | Thread-safe log lines with a timestamp and namespace. |
| [`x-mori-git-repo-info`](https://github.com/x-mori/minimori-packages/tree/main/packages/rubygems/x-mori-git-repo-info#readme) | Owner and repository name from a GitHub remote URL. |
| [`x-mori-once-async`](https://github.com/x-mori/minimori-packages/tree/main/packages/rubygems/x-mori-once-async#readme) | Run a block once and share the result across threads. |
| [`x-mori-package-version`](https://github.com/x-mori/minimori-packages/tree/main/packages/rubygems/x-mori-package-version#readme) | Installed version of a gem. |
| [`x-mori-port-check`](https://github.com/x-mori/minimori-packages/tree/main/packages/rubygems/x-mori-port-check#readme) | Check and find free local TCP ports. |
| [`x-mori-redact-secrets`](https://github.com/x-mori/minimori-packages/tree/main/packages/rubygems/x-mori-redact-secrets#readme) | Hide secrets in nested data before logging. |
| [`xmori-copy-to-clipboard-cli`](https://github.com/x-mori/minimori-packages/tree/main/packages/rubygems/xmori-copy-to-clipboard-cli#readme) | Copy text to the clipboard, with an `xmori-copy` command. |

## Use a package

GitHub Packages requires authentication to install packages, including public ones. Create a personal access token (classic) with the `read:packages` scope and keep it in your package manager's user configuration, never in a repository. Each package README shows the full setup; in short:

| Registry | Configure once | Then install |
| --- | --- | --- |
| npm | `.npmrc`: `@x-mori:registry=https://npm.pkg.github.com` and `//npm.pkg.github.com/:_authToken=${GITHUB_TOKEN}` | `npm install @x-mori/env-required` |
| Maven | Repository `https://maven.pkg.github.com/x-mori/minimori-packages` in `pom.xml`, credentials in `~/.m2/settings.xml` | `io.github.xmori:initials-from-name:1.1.0` |
| NuGet | `dotnet nuget add source https://nuget.pkg.github.com/x-mori/index.json --name github-x-mori --username USER --password TOKEN` | `dotnet add package XMori.IsPrivateIp --version 1.1.0` |
| RubyGems | `bundle config set --global https://rubygems.pkg.github.com/x-mori USER:TOKEN` | `source 'https://rubygems.pkg.github.com/x-mori' do gem 'x-mori-port-check' end` |

GitHub's registry guides: [npm](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-npm-registry), [Maven](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry), [NuGet](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-nuget-registry), [RubyGems](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-rubygems-registry).

## Repository layout

Each directory under `packages/` has its own package manifest, source, tests, and README. Packages have no runtime dependencies. npm packages include TypeScript declarations. The Maven root `pom.xml` only aggregates modules; it is not a nineteenth package.

Each package's project URL (`homepage`, `<url>`, `PackageProjectUrl`, or `spec.homepage`) points to its own folder, so the link on its package page opens its README. The README also ships inside the npm tarball, the NuGet package, and the gem.

## Edit Ruby packages

Install Ruby 3.3 and Bundler, then run `bundle install` at the repository root. The root `Gemfile` includes all seven local gems and Ruby LSP so the editor can index them together. The `.ruby-version` file records the expected Ruby line. This editor bundle does not change the published gems' runtime dependencies. After changing a gem's version, run `bundle lock` so `Gemfile.lock` matches.

If Ruby LSP cannot find Ruby on Windows, use **Select Ruby manually** in its notification and choose your Ruby installation's `bin/ruby.exe`. Reopen VS Code after installing Ruby so its extensions receive the updated PATH.

## Check the repository

```sh
node scripts/check-inventory.mjs
pnpm install --frozen-lockfile
pnpm test:npm
mvn test
dotnet run --project tests/nuget-smoke/NugetSmoke.csproj -c Release
for dir in packages/rubygems/*; do (cd "$dir" && ruby -Ilib test/test.rb); done
```

The GitHub Actions `Check packages` workflow runs those checks and builds package archives. The `Publish packages` workflow runs on manual dispatch after CI passes. Its token needs `packages: write`, which the workflow declares. Publication uses the repository's `GITHUB_TOKEN` and each package's own registry endpoint.

## Verify publication

`node scripts/check-inventory.mjs` proves the source split. After a publish run, `node scripts/check-published.mjs` queries GitHub's package API and checks every distinct package name in the repository. The `Verify published packages` workflow runs the same check with a token that has `packages: read`. It reports missing names instead of counting source folders as published packages.

For later releases, increment the version in each changed package before dispatching the publish workflow again. GitHub Packages counts a new name as a new package; another version of the same name remains one package listing.

## License

MIT
