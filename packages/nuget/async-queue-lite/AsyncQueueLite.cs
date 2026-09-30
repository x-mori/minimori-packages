namespace XMori.AsyncQueueLite;
/// <summary>Limits how many asynchronous operations run at the same time.</summary>
/// <remarks>
/// Callers beyond the limit wait without blocking a thread. Waiting callers are not guaranteed to start in
/// first-in, first-out order. One instance is safe to share between threads.
/// </remarks>
/// <example>
/// <code>
/// using var queue = new AsyncQueue(4);
/// var pages = await Task.WhenAll(urls.Select(url => queue.EnqueueAsync(() => client.GetStringAsync(url))));
/// </code>
/// </example>
public sealed class AsyncQueue : IDisposable {
    private readonly SemaphoreSlim slots;
    /// <summary>Creates a queue that allows at most <paramref name="concurrency"/> operations at once.</summary>
    /// <param name="concurrency">Maximum number of operations running at the same time; at least 1.</param>
    /// <exception cref="ArgumentOutOfRangeException"><paramref name="concurrency"/> is less than 1.</exception>
    public AsyncQueue(int concurrency) {
        ArgumentOutOfRangeException.ThrowIfLessThan(concurrency, 1);
        slots = new SemaphoreSlim(concurrency, concurrency);
    }
    /// <summary>Waits for a free slot, runs the operation, and frees the slot when the operation finishes or fails.</summary>
    /// <typeparam name="T">Result type of the operation.</typeparam>
    /// <param name="action">Asynchronous operation to run. It is not called until a slot is free.</param>
    /// <param name="cancellationToken">Cancels waiting for a slot. It is not passed to <paramref name="action"/>; capture it in the lambda if the operation should observe it too.</param>
    /// <returns>The operation's result. Exceptions from the operation propagate unchanged.</returns>
    /// <exception cref="ArgumentNullException"><paramref name="action"/> is <see langword="null"/>.</exception>
    /// <exception cref="OperationCanceledException">The token was cancelled before a slot became free.</exception>
    /// <exception cref="ObjectDisposedException">The queue has been disposed.</exception>
    public async Task<T> EnqueueAsync<T>(Func<Task<T>> action, CancellationToken cancellationToken = default) {
        ArgumentNullException.ThrowIfNull(action);
        await slots.WaitAsync(cancellationToken).ConfigureAwait(false);
        try { return await action().ConfigureAwait(false); }
        finally { slots.Release(); }
    }
    /// <summary>Releases the underlying semaphore immediately.</summary>
    /// <remarks>Dispose does not wait for queued or running operations. Await their tasks first; an operation still running when the queue is disposed throws <see cref="ObjectDisposedException"/> when it tries to free its slot.</remarks>
    public void Dispose() => slots.Dispose();
}
