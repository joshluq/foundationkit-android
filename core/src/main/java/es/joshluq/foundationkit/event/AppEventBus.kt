package es.joshluq.foundationkit.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

/**
 * Lightweight, type-safe reactive event bus for decoupling cross-cutting communication.
 */
interface AppEventBus {
    /**
     * Publishes an event asynchronously, suspending if the buffer is full.
     */
    suspend fun publish(event: AppEvent)

    /**
     * Attempts to emit an event synchronously without suspending.
     *
     * @return `true` if emitted successfully, `false` otherwise.
     */
    fun tryPublish(event: AppEvent): Boolean

    /**
     * Flow of all published events.
     */
    val events: Flow<AppEvent>
}

/**
 * Subscribes exclusively to events matching the specified type [T].
 */
inline fun <reified T : AppEvent> AppEventBus.subscribe(): Flow<T> = events.filterIsInstance<T>()
