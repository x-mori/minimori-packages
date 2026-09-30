using System.Net;
using System.Net.Sockets;
namespace XMori.IsPrivateIp;
/// <summary>Classifies IP addresses as local or internal versus public.</summary>
/// <example>
/// <code>
/// IpClassifier.IsPrivate("192.168.1.10"); // true
/// IpClassifier.IsPrivate("::ffff:10.0.0.1"); // true (IPv4-mapped)
/// IpClassifier.IsPrivate("8.8.8.8");       // false
/// </code>
/// </example>
public static class IpClassifier {
    /// <summary>Reports whether text is an IP address in a loopback, private, link-local, carrier-grade NAT, or unspecified range.</summary>
    /// <param name="input">IPv4 or IPv6 text. Null, empty, or invalid text returns <see langword="false"/>.</param>
    /// <returns>
    /// <see langword="true"/> for these ranges:
    /// IPv4 <c>0.0.0.0/8</c>, <c>10.0.0.0/8</c>, <c>100.64.0.0/10</c>, <c>127.0.0.0/8</c>, <c>169.254.0.0/16</c>, <c>172.16.0.0/12</c>, and <c>192.168.0.0/16</c>;
    /// IPv6 <c>::</c>, <c>::1</c>, <c>fc00::/7</c> (unique local), and <c>fe80::/10</c> (link-local).
    /// Otherwise <see langword="false"/>.
    /// </returns>
    /// <remarks>
    /// <para>IPv4-mapped IPv6 addresses such as <c>::ffff:10.0.0.1</c> are checked as IPv4. Parsing uses
    /// <see cref="IPAddress.TryParse(string?, out IPAddress?)"/>, which also accepts shorthand forms such as <c>127.1</c>
    /// and zone IDs such as <c>fe80::1%eth0</c>. Surrounding whitespace is not trimmed.</para>
    /// <para>This check alone is not a complete server-side request forgery (SSRF) defense. Resolve the host name
    /// yourself, check every resolved address, and connect to the checked address so DNS cannot change between
    /// the check and the request.</para>
    /// </remarks>
    public static bool IsPrivate(string? input) {
        if (!IPAddress.TryParse(input, out var address)) return false;
        if (address.IsIPv4MappedToIPv6) address = address.MapToIPv4();
        Span<byte> bytes = stackalloc byte[16];
        address.TryWriteBytes(bytes, out _);
        if (address.AddressFamily == AddressFamily.InterNetwork) {
            byte a = bytes[0], b = bytes[1];
            return a is 0 or 10 or 127 ||
                (a == 100 && b is >= 64 and <= 127) ||
                (a == 169 && b == 254) ||
                (a == 172 && b is >= 16 and <= 31) ||
                (a == 192 && b == 168);
        }
        return address.Equals(IPAddress.IPv6Any) || address.Equals(IPAddress.IPv6Loopback) ||
            (bytes[0] & 0xfe) == 0xfc ||
            (bytes[0] == 0xfe && (bytes[1] & 0xc0) == 0x80);
    }
}
