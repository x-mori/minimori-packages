using System.Collections.Concurrent;
using System.Diagnostics;
namespace XMori.MemoizeAsync;
/// <summary>Caches successful async results by key for a fixed time, and shares in-flight work between concurrent callers.</summary>
/// <typeparam name="TKey">Cache key type. Keys are compared with the default equality comparer.</typeparam>
/// <typeparam name="TValue">Cached result type.</typeparam>
/// <remarks>
/// <para>For each key the factory runs at most once at a time: callers that arrive while it runs await the same task.
/// A successful result is reused until the time-to-live expires. A failed or cancelled result is removed, so the next
/// call runs the factory again.</para>
/// <para>Expiry uses a monotonic clock, so changes to the system time do not affect it. Expired entries are replaced when
/// their key is requested again but are not removed in the background; call <see cref="Clear"/> if the key space is
/// unbounded. One instance is safe to share between threads.</para>
/// </remarks>
/// <example>
/// <code>
/// var users = new AsyncMemoizer&lt;int, User&gt;(id =&gt; api.GetUserAsync(id), TimeSpan.FromMinutes(5));
/// var user = await users.GetAsync(7); // calls the API
/// var same = await users.GetAsync(7); // served from the cache for five minutes
/// </code>
/// </example>
public sealed class AsyncMemoizer<TKey, TValue> where TKey : notnull {
    private sealed record Entry(long ExpiresAt, Lazy<Task<TValue>> Value);
    private readonly ConcurrentDictionary<TKey, Entry> entries = new();
    private readonly Func<TKey, Task<TValue>> factory;
    private readonly long ttlTicks;
    /// <summary>Creates a memoizer with a positive time-to-live.</summary>
    /// <param name="factory">Async function called for a key that is missing or expired.</param>
    /// <param name="ttl">How long a result stays cached, measured from when the factory call started.</param>
    /// <exception cref="ArgumentNullException"><paramref name="factory"/> is <see langword="null"/>.</exception>
    /// <exception cref="ArgumentOutOfRangeException"><paramref name="ttl"/> is zero or negative.</exception>
    public AsyncMemoizer(Func<TKey, Task<TValue>> factory, TimeSpan ttl) {
        this.factory = factory ?? throw new ArgumentNullException(nameof(factory));
        if (ttl <= TimeSpan.Zero) throw new ArgumentOutOfRangeException(nameof(ttl));
        var ticks = ttl.TotalSeconds * Stopwatch.Frequency;
        ttlTicks = ticks >= long.MaxValue / 2 ? long.MaxValue / 2 : Math.Max(1, (long)ticks);
    }
    /// <summary>Returns the cached result for a key, or runs the factory once and shares its task with concurrent callers.</summary>
    /// <param name="key">Key identifying the work.</param>
    /// <returns>The cached or newly computed result. A cache hit on a completed result returns the stored task without extra allocation.</returns>
    /// <exception cref="ArgumentNullException"><paramref name="key"/> is <see langword="null"/>.</exception>
    /// <remarks>Exceptions from the factory propagate to every caller that shared that attempt.</remarks>
    public Task<TValue> GetAsync(TKey key) {
        ArgumentNullException.ThrowIfNull(key);
        while (true) {
            var now = Stopwatch.GetTimestamp();
            if (entries.TryGetValue(key, out var old) && old.ExpiresAt > now) return Resolve(key, old);
            var fresh = new Entry(now + ttlTicks, new Lazy<Task<TValue>>(() => factory(key)));
            if (old is null ? entries.TryAdd(key, fresh) : entries.TryUpdate(key, fresh, old)) return Resolve(key, fresh);
        }
    }
    private Task<TValue> Resolve(TKey key, Entry entry) {
        Task<TValue> task;
        try { task = entry.Value.Value; }
        catch (Exception exception) {
            Forget(key, entry);
            return Task.FromException<TValue>(exception);
        }
        return task.IsCompletedSuccessfully ? task : Observe(key, entry, task);
    }
    private async Task<TValue> Observe(TKey key, Entry entry, Task<TValue> task) {
        try { return await task.ConfigureAwait(false); }
        catch { Forget(key, entry); throw; }
    }
    private void Forget(TKey key, Entry entry) => entries.TryRemove(new KeyValuePair<TKey, Entry>(key, entry));
    /// <summary>Removes every cached entry. Work already in flight continues, and its callers still receive its result.</summary>
    public void Clear() => entries.Clear();
}
