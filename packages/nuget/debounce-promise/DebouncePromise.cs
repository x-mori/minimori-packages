namespace XMori.DebouncePromise;
/// <summary>Runs an async action once after calls stop arriving for a quiet period, and gives every caller in that burst the same result.</summary>
/// <typeparam name="TArg">Argument type passed to the action.</typeparam>
/// <typeparam name="TResult">Result type of the action.</typeparam>
/// <remarks>
/// <para>Each call to <see cref="RunAsync"/> restarts the quiet-period timer and replaces the pending argument. When the
/// timer expires, the action runs once with the latest argument, and every task returned during that burst completes
/// with its result or exception. Calls made after the action has started begin a new burst.</para>
/// <para>One instance is safe to use from multiple threads. With a positive delay the action starts on the thread
/// pool; with a zero delay it may start on the calling thread. No lock is held while the action runs.</para>
/// </remarks>
/// <example>
/// <code>
/// var search = new AsyncDebouncer&lt;string, SearchResult&gt;(query =&gt; api.SearchAsync(query), TimeSpan.FromMilliseconds(300));
/// // Typing "c", "ca", "cat" quickly sends one request for "cat"; all three awaits receive its result.
/// var result = await search.RunAsync(textBox.Text);
/// </code>
/// </example>
public sealed class AsyncDebouncer<TArg, TResult> {
    private readonly Func<TArg, Task<TResult>> action;
    private readonly TimeSpan delay;
    private readonly object gate = new();
    private CancellationTokenSource? timer;
    private TaskCompletionSource<TResult>? pending;
    /// <summary>Creates a debouncer for one async action.</summary>
    /// <param name="action">Function to run after the quiet period, with the latest argument.</param>
    /// <param name="delay">Quiet period that must pass without another call; zero or greater.</param>
    /// <exception cref="ArgumentNullException"><paramref name="action"/> is <see langword="null"/>.</exception>
    /// <exception cref="ArgumentOutOfRangeException"><paramref name="delay"/> is negative.</exception>
    public AsyncDebouncer(Func<TArg, Task<TResult>> action, TimeSpan delay) {
        this.action = action ?? throw new ArgumentNullException(nameof(action));
        ArgumentOutOfRangeException.ThrowIfLessThan(delay, TimeSpan.Zero);
        this.delay = delay;
    }
    /// <summary>Schedules the action with <paramref name="argument"/> and returns a task shared by every caller in the current burst.</summary>
    /// <param name="argument">Argument to use if no newer call arrives before the quiet period ends.</param>
    /// <returns>A task that completes with the action's result, or faults with its exception, once the burst's action finishes.</returns>
    public Task<TResult> RunAsync(TArg argument) {
        CancellationTokenSource source;
        CancellationToken token;
        TaskCompletionSource<TResult> completion;
        lock (gate) {
            timer?.Cancel();
            timer?.Dispose();
            source = new CancellationTokenSource();
            token = source.Token; // read under the lock; a later call may dispose the source
            timer = source;
            completion = pending ??= new(TaskCreationOptions.RunContinuationsAsynchronously);
        }
        // Started outside the lock so a zero delay never runs the action while the lock is held.
        _ = FireAsync(argument, source, completion, token);
        return completion.Task;
    }
    private async Task FireAsync(TArg argument, CancellationTokenSource source, TaskCompletionSource<TResult> completion, CancellationToken token) {
        // A superseded call is the common case, so observe cancellation without throwing an exception.
        await Task.Delay(delay, token).ConfigureAwait(ConfigureAwaitOptions.SuppressThrowing);
        lock (gate) {
            if (token.IsCancellationRequested || !ReferenceEquals(pending, completion)) return;
            pending = null;
            timer = null;
        }
        source.Dispose();
        try { completion.TrySetResult(await action(argument).ConfigureAwait(false)); }
        catch (Exception exception) { completion.TrySetException(exception); }
    }
}
