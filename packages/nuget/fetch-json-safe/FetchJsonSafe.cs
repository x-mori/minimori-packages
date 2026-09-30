using System.Net;
using System.Net.Http.Json;
namespace XMori.FetchJsonSafe;
/// <summary>The outcome of <see cref="JsonFetch.GetAsync{T}"/>.</summary>
/// <param name="Success"><see langword="true"/> when the server returned a 2xx status and the body was read as JSON.</param>
/// <param name="Data">The deserialized body on success; otherwise the default value. A JSON body of <c>null</c> also gives the default.</param>
/// <param name="Status">HTTP status when a response was received, including when its body was not valid JSON; <see langword="null"/> for network errors and timeouts.</param>
/// <param name="Error">A short failure message such as <c>"HTTP 404"</c> or the exception message; <see langword="null"/> on success.</param>
/// <typeparam name="T">Expected JSON payload type.</typeparam>
public sealed record JsonFetchResult<T>(bool Success, T? Data, HttpStatusCode? Status, string? Error);
/// <summary>Sends JSON GET requests and reports every expected failure as data instead of an exception.</summary>
/// <example>
/// <code>
/// var result = await JsonFetch.GetAsync&lt;User&gt;(client, new Uri("https://api.example.com/users/7"));
/// if (result.Success) Console.WriteLine(result.Data!.Name);
/// else Console.WriteLine($"Failed ({result.Status}): {result.Error}");
/// </code>
/// </example>
public static class JsonFetch {
    /// <summary>Sends a GET request and deserializes a successful JSON response.</summary>
    /// <typeparam name="T">Expected response body type.</typeparam>
    /// <param name="client">HTTP client used for the request. Its base address, headers, and timeout apply.</param>
    /// <param name="uri">Request URI; relative URIs use the client's <see cref="HttpClient.BaseAddress"/>.</param>
    /// <param name="cancellationToken">Token that cancels the request.</param>
    /// <returns>
    /// A success result with the data, or a failure result for a non-2xx status (the body is not read), a network
    /// error, an <see cref="HttpClient.Timeout"/> expiry, invalid JSON, or an unsupported content type.
    /// </returns>
    /// <remarks>
    /// The whole body is downloaded within the client's timeout, then deserialized with <c>System.Text.Json</c>
    /// web defaults (camelCase, case-insensitive). When the body is not valid JSON, the result keeps the HTTP status.
    /// </remarks>
    /// <exception cref="ArgumentNullException"><paramref name="client"/> or <paramref name="uri"/> is <see langword="null"/>.</exception>
    /// <exception cref="OperationCanceledException"><paramref name="cancellationToken"/> was cancelled. Caller cancellation is never turned into a result.</exception>
    public static async Task<JsonFetchResult<T>> GetAsync<T>(HttpClient client, Uri uri, CancellationToken cancellationToken = default) {
        ArgumentNullException.ThrowIfNull(client); ArgumentNullException.ThrowIfNull(uri);
        HttpStatusCode? status = null;
        try {
            using var response = await client.GetAsync(uri, cancellationToken).ConfigureAwait(false);
            status = response.StatusCode;
            if (!response.IsSuccessStatusCode) return new(false, default, status, $"HTTP {(int)status}");
            var data = await response.Content.ReadFromJsonAsync<T>(cancellationToken).ConfigureAwait(false);
            return new(true, data, status, null);
        } catch (OperationCanceledException) when (cancellationToken.IsCancellationRequested) { throw; }
        catch (Exception exception) when (exception is HttpRequestException or OperationCanceledException or System.Text.Json.JsonException or NotSupportedException) {
            return new(false, default, status, exception.Message);
        }
    }
}
