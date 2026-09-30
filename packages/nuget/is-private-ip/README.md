# XMori.IsPrivateIp

Check whether an IP address is loopback, private, link-local, carrier-grade NAT, or unspecified — for example, to refuse requests to internal addresses when fetching user-supplied URLs.

```csharp
using XMori.IsPrivateIp;

IpClassifier.IsPrivate("192.168.1.10");    // true
IpClassifier.IsPrivate("::ffff:10.0.0.1"); // true (IPv4-mapped IPv6)
IpClassifier.IsPrivate("8.8.8.8");         // false
```

## Install

Targets .NET 8; no dependencies beyond the framework.

The package is published to GitHub Packages, which requires authentication even for public packages. Add the feed once with a token that has the `read:packages` scope, then add the package:

```sh
dotnet nuget add source "https://nuget.pkg.github.com/x-mori/index.json" --name github-x-mori --username YOUR_GITHUB_USERNAME --password YOUR_TOKEN
dotnet add package XMori.IsPrivateIp --version 1.1.0
```

On macOS and Linux, add `--store-password-in-clear-text` to the first command, or keep the token in an environment variable referenced from `nuget.config`.

## API

### `static bool IpClassifier.IsPrivate(string? input)`

Returns `true` when `input` parses as an address in one of these ranges:

| Range | Meaning |
| --- | --- |
| `0.0.0.0/8` | "this network", including `0.0.0.0` |
| `10.0.0.0/8`, `172.16.0.0/12`, `192.168.0.0/16` | private (RFC 1918) |
| `100.64.0.0/10` | carrier-grade NAT (RFC 6598) |
| `127.0.0.0/8` | loopback |
| `169.254.0.0/16` | link-local, including cloud metadata endpoints such as `169.254.169.254` |
| `::` | IPv6 unspecified |
| `::1` | IPv6 loopback |
| `fc00::/7` | IPv6 unique local |
| `fe80::/10` | IPv6 link-local |

Returns `false` for every other address, and for `null`, empty, or unparseable text.

- IPv4-mapped IPv6 addresses (`::ffff:a.b.c.d`) are checked as IPv4.
- Parsing uses `IPAddress.TryParse`, so shorthand forms that operating systems also accept, such as `127.1` or `0x7f.0.0.1`, are recognized. Zone IDs such as `fe80::1%eth0` are allowed. Whitespace is not trimmed.

The method is stateless and thread-safe.

## Using it against SSRF

Checking the host name in a URL is not enough, because DNS can point any name at an internal address, and can change between your check and the request. Resolve the name yourself, check **every** resolved address, and connect to the address you checked:

```csharp
var addresses = await Dns.GetHostAddressesAsync(uri.Host);
if (addresses.Length == 0 || addresses.Any(address => IpClassifier.IsPrivate(address.ToString())))
    throw new InvalidOperationException("Refusing to fetch an internal address.");
```

With `HttpClient`, the most robust approach is a `SocketsHttpHandler.ConnectCallback` that performs this check on the address actually being connected. Also consider blocking other special-purpose ranges your network uses.

## Changes in 1.1.0

- The IPv6 unspecified address `::` is now classified as private, consistent with IPv4 `0.0.0.0`.
- Address bytes are written to a stack buffer instead of a new array on each call.

## License

MIT
