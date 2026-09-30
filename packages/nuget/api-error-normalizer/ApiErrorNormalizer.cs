using System.Net;
namespace XMori.ApiErrorNormalizer;
/// <summary>A common error shape for HTTP failures, cancellations, and other exceptions.</summary>
/// <param name="Message">Human-readable error text taken from the exception, or the caller's message.</param>
/// <param name="StatusCode">HTTP status code when one is known; otherwise <see langword="null"/>.</param>
/// <param name="Kind">
/// Error category: <c>"http"</c> for <see cref="HttpRequestException"/> and <see cref="ApiErrors.FromStatus"/>,
/// <c>"cancelled"</c> for <see cref="OperationCanceledException"/> (including <see cref="HttpClient"/> timeouts),
/// or <c>"unknown"</c> for anything else.
/// </param>
public sealed record NormalizedApiError(string Message, int? StatusCode, string Kind);
/// <summary>Converts request failures to one stable <see cref="NormalizedApiError"/> shape for logging and API responses.</summary>
/// <example>
/// <code>
/// try { await client.GetStringAsync(uri); }
/// catch (Exception exception) {
///     var error = ApiErrors.Normalize(exception);
///     logger.LogWarning("{Kind} {Status}: {Message}", error.Kind, error.StatusCode, error.Message);
/// }
/// </code>
/// </example>
public static class ApiErrors {
    /// <summary>Classifies an exception without discarding its message or HTTP status.</summary>
    /// <param name="error">Exception to classify.</param>
    /// <returns>
    /// A record whose kind is <c>"http"</c> for <see cref="HttpRequestException"/> (with its status code, which is
    /// <see langword="null"/> for network failures that produced no response), <c>"cancelled"</c> for
    /// <see cref="OperationCanceledException"/> and its subclasses, and <c>"unknown"</c> otherwise.
    /// </returns>
    /// <remarks>The exception's own message is copied as-is. Do not return it to untrusted clients if it may contain internal details.</remarks>
    /// <exception cref="ArgumentNullException"><paramref name="error"/> is <see langword="null"/>.</exception>
    public static NormalizedApiError Normalize(Exception error) {
        ArgumentNullException.ThrowIfNull(error);
        return error switch {
            HttpRequestException http => new(http.Message, (int?)http.StatusCode, "http"),
            OperationCanceledException => new(error.Message, null, "cancelled"),
            _ => new(error.Message, null, "unknown")
        };
    }
    /// <summary>Builds an HTTP error record from a status code, for example after checking <see cref="HttpResponseMessage.StatusCode"/>.</summary>
    /// <param name="status">HTTP status code.</param>
    /// <param name="message">Optional message; defaults to <c>"HTTP {code}"</c>, such as <c>"HTTP 404"</c>.</param>
    /// <returns>A record with kind <c>"http"</c> and the numeric status code.</returns>
    public static NormalizedApiError FromStatus(HttpStatusCode status, string? message = null) =>
        new(message ?? $"HTTP {(int)status}", (int)status, "http");
}
