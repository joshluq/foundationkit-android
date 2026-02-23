package es.joshluq.foundationkit.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Interface that provides access to Coroutine Dispatchers.
 *
 * Using this abstraction instead of [Dispatchers] directly is crucial for Unit Testing,
 * as it allows replacing real dispatchers with TestDispatchers to ensure
 * synchronization and determinism in tests.
 */
interface DispatcherProvider {
    /** Main thread dispatcher for UI-related tasks. */
    val main: CoroutineDispatcher

    /** Dispatcher optimized for disk and network IO operations. */
    val io: CoroutineDispatcher

    /** Dispatcher optimized for CPU-intensive tasks. */
    val default: CoroutineDispatcher

    /** Dispatcher that executes on the current thread until the first suspension. */
    val unconfined: CoroutineDispatcher
}

/**
 * Default implementation of [DispatcherProvider] using standard [Dispatchers].
 */
class DefaultDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}
