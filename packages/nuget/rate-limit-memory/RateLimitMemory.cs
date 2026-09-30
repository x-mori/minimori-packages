using System.Runtime.InteropServices;
namespace XMori.RateLimitMemory;
/// <summary>Enforces a fixed-window request limit per string key, in memory.</summary>
/// <remarks>
/// <para>Each key gets its own window, which starts at that key's first request. Up to the limit of requests are
/// allowed in the window; further requests are refused until the window has fully elapsed, and the next request then
/// starts a new window. Because windows are fixed, a client can make up to twice the limit in a short burst that spans
/// a window boundary.</para>
/// <para>State lives in this process only, so each server instance counts separately. Entries are never removed
/// automatically; call <see cref="RemoveExpired"/> periodically when keys are unbounded, such as IP addresses.
/// One instance is safe to share between threads.</para>
/// </remarks>
/// <example>
/// <code>
/// var limiter = new MemoryRateLimiter(limit: 5, window: TimeSpan.FromMinutes(1));
/// if (!limiter.TryAcquire(clientIp)) return Results.StatusCode(429);
/// </code>
/// </example>
public sealed class MemoryRateLimiter {
    private struct Window { public DateTimeOffset Start; public int Count; }
    private readonly Dictionary<string, Window> entries = new();
    private readonly object gate = new();
    private readonly int limit;
    private readonly TimeSpan window;
    /// <summary>Creates a limiter that allows <paramref name="limit"/> requests per key in each <paramref name="window"/>.</summary>
    /// <param name="limit">Requests allowed per key and window; at least 1.</param>
    /// <param name="window">Window length; greater than zero.</param>
    /// <exception cref="ArgumentOutOfRangeException"><paramref name="limit"/> is less than 1, or <paramref name="window"/> is zero or negative.</exception>
    public MemoryRateLimiter(int limit, TimeSpan window) {
        ArgumentOutOfRangeException.ThrowIfLessThan(limit, 1);
        if (window <= TimeSpan.Zero) throw new ArgumentOutOfRangeException(nameof(window));
        this.limit = limit; this.window = window;
    }
    /// <summary>Uses one request slot for <paramref name="key"/> if its current window has capacity.</summary>
    /// <param name="key">Caller-defined identity to limit, such as a user ID, API key, or IP address. Compared ordinally.</param>
    /// <param name="now">Clock value to use instead of <see cref="DateTimeOffset.UtcNow"/>; useful for tests.</param>
    /// <returns><see langword="true"/> if the request is allowed; <see langword="false"/> if the key has reached its limit. A refused request does not count.</returns>
    /// <exception cref="ArgumentNullException"><paramref name="key"/> is <see langword="null"/>.</exception>
    public bool TryAcquire(string key, DateTimeOffset? now = null) {
        ArgumentNullException.ThrowIfNull(key);
        var time = now ?? DateTimeOffset.UtcNow;
        lock (gate) {
            ref var state = ref CollectionsMarshal.GetValueRefOrAddDefault(entries, key, out bool exists);
            if (!exists || time - state.Start >= window) { state.Start = time; state.Count = 0; }
            if (state.Count >= limit) return false;
            state.Count++;
            return true;
        }
    }
    /// <summary>Removes keys whose window has fully elapsed, freeing their memory.</summary>
    /// <param name="now">Clock value to use instead of <see cref="DateTimeOffset.UtcNow"/>.</param>
    /// <returns>The number of removed keys.</returns>
    /// <remarks>Removing an expired key does not change any result: its next request starts a new window either way.</remarks>
    public int RemoveExpired(DateTimeOffset? now = null) {
        var time = now ?? DateTimeOffset.UtcNow;
        lock (gate) {
            int removed = 0;
            foreach (var (key, state) in entries) {
                if (time - state.Start >= window && entries.Remove(key)) removed++;
            }
            return removed;
        }
    }
}
