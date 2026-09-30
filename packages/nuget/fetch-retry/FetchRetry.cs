using System.Net;
namespace XMori.FetchRetry;
/// <summary>Retries idempotent HTTP GET requests after transient failures, with exponential backoff.</summary>
/// <example>
/// <code>
/// using var response = await RetryingFetch.GetAsync(client, new Uri("https://api.example.com/status"), attempts: 4, delay: TimeSpan.FromMilliseconds(250));
/// response.EnsureSuccessStatusCode();
/// </code>
/// </example>
public static class RetryingFetch {
    private static readonly TimeSpan MaxDelay = TimeSpan.FromSeconds(30);
    /// <summary>Sends a GET request, retrying after HTTP 429, any HTTP 5xx status, or an <see cref="HttpRequestException"/>.</summary>
    /// <param name="client">HTTP client used for every attempt.</param>
    /// <param name="uri">Request URI; relative URIs use the client's <see cref="HttpClient.BaseAddress"/>.</param>
    /// <param name="attempts">Maximum number of requests, including the first; at least 1. Default 3.</param>
    /// <param name="delay">
    /// Wait before the second request; zero or greater. Default 200 ms. Each later wait doubles, up to 30 seconds,
    /// so the defaults wait 200 ms and then 400 ms.
    /// </param>
    /// <param name="cancellationToken">Cancels the current request or backoff wait.</param>
    /// <returns>
    /// The first response that is not retryable, or the last response once attempts run out, even if it is a
    /// failure status. The caller owns the response and must dispose it.
    /// </returns>
    /// <remarks>
    /// Responses that trigger a retry are disposed before waiting. A <c>Retry-After</c> header is not read; choose a
    /// delay suitable for the server's rate limits. Other 4xx statuses are returned at once because repeating them
    /// will not help. Only GET is sent, so retries are safe for servers that follow HTTP semantics.
    /// </remarks>
    /// <exception cref="ArgumentNullException"><paramref name="client"/> or <paramref name="uri"/> is <see langword="null"/>.</exception>
    /// <exception cref="ArgumentOutOfRangeException"><paramref name="attempts"/> is less than 1 or <paramref name="delay"/> is negative.</exception>
    /// <exception cref="HttpRequestException">The final attempt failed without a response.</exception>
    /// <exception cref="OperationCanceledException">The token was cancelled, or the client's timeout expired.</exception>
    public static async Task<HttpResponseMessage> GetAsync(HttpClient client, Uri uri, int attempts = 3, TimeSpan? delay = null, CancellationToken cancellationToken = default) {
        ArgumentNullException.ThrowIfNull(client); ArgumentNullException.ThrowIfNull(uri);
        ArgumentOutOfRangeException.ThrowIfLessThan(attempts, 1);
        var wait = delay ?? TimeSpan.FromMilliseconds(200);
        if (wait < TimeSpan.Zero) throw new ArgumentOutOfRangeException(nameof(delay));
        for (int attempt = 1; ; attempt++) {
            HttpResponseMessage? response = null;
            try {
                response = await client.GetAsync(uri, cancellationToken).ConfigureAwait(false);
                if (attempt == attempts || !IsTransient(response.StatusCode)) return response;
            } catch (HttpRequestException) when (attempt < attempts) { }
            response?.Dispose();
            if (wait > TimeSpan.Zero) await Task.Delay(wait, cancellationToken).ConfigureAwait(false);
            wait = wait >= MaxDelay / 2 ? MaxDelay : wait * 2;
        }
    }
    private static bool IsTransient(HttpStatusCode status) => status == HttpStatusCode.TooManyRequests || (int)status >= 500;
}
