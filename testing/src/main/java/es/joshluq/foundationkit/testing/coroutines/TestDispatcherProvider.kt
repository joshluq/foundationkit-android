package es.joshluq.foundationkit.testing.coroutines

import es.joshluq.foundationkit.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

/**
 * Implementation of [DispatcherProvider] for unit testing.
 *
 * Allows controlling virtual time and executing coroutines deterministically.
 * By default, all dispatchers route to the provided [testDispatcher].
 *
 * @property testDispatcher The test dispatcher used for coroutines (defaults to [StandardTestDispatcher]).
 */
class TestDispatcherProvider(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : DispatcherProvider {
    override val main: CoroutineDispatcher = testDispatcher
    override val io: CoroutineDispatcher = testDispatcher
    override val default: CoroutineDispatcher = testDispatcher
    override val unconfined: CoroutineDispatcher = testDispatcher
}
