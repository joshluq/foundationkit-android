package es.joshluq.foundationkit.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Returns a [Flow] that emits only the first item emitted by the source flow within a time window of
 * [windowDurationMillis], discarding any subsequent emissions that occur during that window.
 *
 * Commonly used to prevent rapid double-clicks or burst event processing in user interfaces.
 *
 * @param windowDurationMillis The duration of the throttling window in milliseconds.
 * @return A throttled [Flow].
 */
fun <T> Flow<T>.throttleFirst(windowDurationMillis: Long): Flow<T> {
    require(windowDurationMillis >= 0) { "windowDurationMillis must be non-negative" }
    return flow {
        var lastEmissionTime = 0L
        collect { value ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEmissionTime >= windowDurationMillis) {
                lastEmissionTime = currentTime
                emit(value)
            }
        }
    }
}
