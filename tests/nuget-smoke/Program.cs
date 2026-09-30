using System.Net;
using System.Net.Http;
using System.Text;

static void Check(bool value, string name)
{
    if (!value) throw new Exception($"NuGet smoke test failed: {name}");
}

foreach (var address in new[] { "10.0.0.1", "172.16.0.1", "192.168.1.10", "127.0.0.1", "169.254.1.1", "100.64.0.1", "0.0.0.0", "::1", "::", "fd00::1", "fe80::1", "::ffff:10.0.0.1" })
    Check(XMori.IsPrivateIp.IpClassifier.IsPrivate(address), $"private IP {address}");
foreach (var address in new[] { "8.8.8.8", "172.32.0.1", "100.128.0.1", "2001:4860:4860::8888", "not an ip", "", null })
    Check(!XMori.IsPrivateIp.IpClassifier.IsPrivate(address), $"public IP {address}");

Check(XMori.ApiErrorNormalizer.ApiErrors.FromStatus(HttpStatusCode.NotFound).StatusCode == 404, "API error");
Check(XMori.ApiErrorNormalizer.ApiErrors.Normalize(new TaskCanceledException()).Kind == "cancelled", "API error cancelled");
Check(XMori.ApiErrorNormalizer.ApiErrors.Normalize(new HttpRequestException("x", null, HttpStatusCode.BadGateway)).StatusCode == 502, "API error status");

using (var handler = new StubHandler(call => new HttpResponseMessage(call == 1 ? HttpStatusCode.ServiceUnavailable : HttpStatusCode.OK)))
using (var client = new HttpClient(handler))
using (var response = await XMori.FetchRetry.RetryingFetch.GetAsync(client, new Uri("https://example.com"), 2, TimeSpan.Zero))
    Check(response.IsSuccessStatusCode && handler.Calls == 2, "HTTP retry");

using (var handler = new StubHandler(_ => new HttpResponseMessage(HttpStatusCode.NotFound)))
using (var client = new HttpClient(handler))
using (var response = await XMori.FetchRetry.RetryingFetch.GetAsync(client, new Uri("https://example.com"), 3, TimeSpan.Zero))
    Check(response.StatusCode == HttpStatusCode.NotFound && handler.Calls == 1, "HTTP retry skips 404");

using (var handler = new StubHandler(_ => new HttpResponseMessage(HttpStatusCode.OK) { Content = new StringContent("{\"value\":7}", Encoding.UTF8, "application/json") }))
using (var client = new HttpClient(handler))
{
    var result = await XMori.FetchJsonSafe.JsonFetch.GetAsync<Payload>(client, new Uri("https://example.com"));
    Check(result.Success && result.Data?.Value == 7 && result.Status == HttpStatusCode.OK, "JSON fetch");
}

using (var handler = new StubHandler(_ => new HttpResponseMessage(HttpStatusCode.OK) { Content = new StringContent("{oops", Encoding.UTF8, "application/json") }))
using (var client = new HttpClient(handler))
{
    var result = await XMori.FetchJsonSafe.JsonFetch.GetAsync<Payload>(client, new Uri("https://example.com"));
    Check(!result.Success && result.Status == HttpStatusCode.OK && result.Error is not null, "JSON fetch invalid body");
}

using (var client = new HttpClient(new SlowHandler()) { Timeout = TimeSpan.FromMilliseconds(20) })
{
    var result = await XMori.FetchJsonSafe.JsonFetch.GetAsync<Payload>(client, new Uri("https://example.com"));
    Check(!result.Success && result.Status is null, "JSON fetch client timeout");
}

using (var client = new HttpClient(new SlowHandler()))
{
    try
    {
        using var response = await XMori.FetchTimeout.TimedFetch.GetAsync(client, new Uri("https://example.com"), TimeSpan.FromMilliseconds(10));
        throw new Exception("timeout did not fire");
    }
    catch (OperationCanceledException) { }
}

var limiter = new XMori.RateLimitMemory.MemoryRateLimiter(2, TimeSpan.FromMinutes(1));
var start = DateTimeOffset.UnixEpoch;
Check(limiter.TryAcquire("x", start) && limiter.TryAcquire("x", start) && !limiter.TryAcquire("x", start.AddSeconds(59)), "rate limiter");
Check(limiter.TryAcquire("y", start), "rate limiter keys are independent");
Check(limiter.TryAcquire("x", start.AddMinutes(1)), "rate limiter window resets");
Check(limiter.RemoveExpired(start.AddSeconds(90)) == 1 && limiter.RemoveExpired(start.AddMinutes(5)) == 1, "rate limiter cleanup");

var debounce = new XMori.DebouncePromise.AsyncDebouncer<int, int>(x => Task.FromResult(x), TimeSpan.FromMilliseconds(10));
var first = debounce.RunAsync(1);
var second = debounce.RunAsync(2);
Check(await first == 2 && await second == 2, "debounce");
Check(await debounce.RunAsync(3) == 3, "debounce new burst");
var failing = new XMori.DebouncePromise.AsyncDebouncer<int, int>(_ => throw new InvalidOperationException("boom"), TimeSpan.Zero);
try { await failing.RunAsync(1); throw new Exception("debounce error did not propagate"); }
catch (InvalidOperationException) { }

using (var queue = new XMori.AsyncQueueLite.AsyncQueue(2))
{
    int running = 0, peak = 0;
    await Task.WhenAll(Enumerable.Range(0, 8).Select(_ => queue.EnqueueAsync(async () =>
    {
        var now = Interlocked.Increment(ref running);
        InterlockedMax(ref peak, now);
        await Task.Delay(5);
        Interlocked.Decrement(ref running);
        return 0;
    })));
    Check(peak <= 2 && peak > 0, "async queue limit");
    Check(await queue.EnqueueAsync(() => Task.FromResult(5)) == 5, "async queue");
}

int calls = 0;
var memo = new XMori.MemoizeAsync.AsyncMemoizer<int, int>(x => Task.FromResult(x + ++calls), TimeSpan.FromMinutes(1));
Check(await memo.GetAsync(1) == await memo.GetAsync(1) && calls == 1, "memoizer");
memo.Clear();
Check(await memo.GetAsync(1) == 3 && calls == 2, "memoizer clear");

int attempts = 0;
var flaky = new XMori.MemoizeAsync.AsyncMemoizer<string, int>(async _ => { await Task.Yield(); if (++attempts == 1) throw new InvalidOperationException(); return attempts; }, TimeSpan.FromMinutes(1));
try { await flaky.GetAsync("k"); throw new Exception("memoizer error did not propagate"); }
catch (InvalidOperationException) { }
Check(await flaky.GetAsync("k") == 2 && await flaky.GetAsync("k") == 2, "memoizer retries after failure");

var shortLived = new XMori.MemoizeAsync.AsyncMemoizer<int, int>(x => Task.FromResult(++calls), TimeSpan.FromMilliseconds(20));
var before = await shortLived.GetAsync(0);
await Task.Delay(50);
Check(await shortLived.GetAsync(0) != before, "memoizer expiry");
Console.WriteLine("NuGet package smoke tests passed.");

static void InterlockedMax(ref int target, int value)
{
    int current;
    while ((current = Volatile.Read(ref target)) < value && Interlocked.CompareExchange(ref target, value, current) != current) { }
}

sealed record Payload(int Value);
sealed class StubHandler(Func<int, HttpResponseMessage> respond) : HttpMessageHandler
{
    public int Calls { get; private set; }
    protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken) =>
        Task.FromResult(respond(++Calls));
}
sealed class SlowHandler : HttpMessageHandler
{
    protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
    {
        await Task.Delay(TimeSpan.FromSeconds(1), cancellationToken);
        return new HttpResponseMessage(HttpStatusCode.OK);
    }
}
