namespace XMori.FetchTimeout;
/// <summary>Adds a per-request deadline to HTTP GET requests without changing the shared client's timeout.</summary>
/// <example>
/// <code>
/// try {
///     using var response = await TimedFetch.GetAsync(client, new Uri("https://example.com/health"), TimeSpan.FromSeconds(2));
///     Console.WriteLine(response.StatusCode);
/// } catch (OperationCanceledException) {
///     Console.WriteLine("Health check timed out or was cancelled.");
/// }
/// </code>
/// </example>
public static class TimedFetch {
    /// <summary>Sends a GET request and cancels it if the full response has not arrived before <paramref name="timeout"/>.</summary>
    /// <param name="client">HTTP client used for the request.</param>
    /// <param name="uri">Request URI; relative URIs use the client's <see cref="HttpClient.BaseAddress"/>.</param>
    /// <param name="timeout">Positive deadline covering connection, headers, and the response body.</param>
    /// <param name="cancellationToken">Optional caller token; cancelling it also cancels the request.</param>
    /// <returns>The HTTP response with its body already buffered, including non-success statuses. The caller must dispose it.</returns>
    /// <remarks>
    /// The shorter of <paramref name="timeout"/> and <see cref="HttpClient.Timeout"/> wins. Both a timeout and
    /// caller cancellation throw <see cref="OperationCanceledException"/> (usually <see cref="TaskCanceledException"/>);
    /// check <paramref name="cancellationToken"/>.<see cref="CancellationToken.IsCancellationRequested"/> to tell them apart.
    /// </remarks>
    /// <exception cref="ArgumentNullException"><paramref name="client"/> or <paramref name="uri"/> is <see langword="null"/>.</exception>
    /// <exception cref="ArgumentOutOfRangeException"><paramref name="timeout"/> is zero or negative.</exception>
    /// <exception cref="OperationCanceledException">The deadline passed or the caller cancelled.</exception>
    /// <exception cref="HttpRequestException">The request failed because of a network or protocol error.</exception>
    public static async Task<HttpResponseMessage> GetAsync(HttpClient client, Uri uri, TimeSpan timeout, CancellationToken cancellationToken = default) {
        ArgumentNullException.ThrowIfNull(client); ArgumentNullException.ThrowIfNull(uri);
        if (timeout <= TimeSpan.Zero) throw new ArgumentOutOfRangeException(nameof(timeout));
        using var linked = CancellationTokenSource.CreateLinkedTokenSource(cancellationToken);
        linked.CancelAfter(timeout);
        return await client.GetAsync(uri, linked.Token).ConfigureAwait(false);
    }
}
