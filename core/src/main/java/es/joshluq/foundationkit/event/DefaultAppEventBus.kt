package es.joshluq.foundationkit.event

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Default implementation of [AppEventBus] powered by [MutableSharedFlow].
 *
 * @param replay Number of past events to replay to new subscribers. Defaults to 0 to prevent re-delivering transient events.
 * @param extraBufferCapacity Extra buffer capacity to prevent suspending publishers under heavy load. Defaults to 64.
 * @param onBufferOverflow Buffer overflow strategy. Defaults to [BufferOverflow.DROP_OLDEST].
 */
class DefaultAppEventBus(
    replay: Int = 0,
    extraBufferCapacity: Int = 64,
    onBufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST
) : AppEventBus {

    private val _events = MutableSharedFlow<AppEvent>(
        replay = replay,
        extraBufferCapacity = extraBufferCapacity,
        onBufferOverflow = onBufferOverflow
    )

    override val events: Flow<AppEvent> = _events.asSharedFlow()

    override suspend fun publish(event: AppEvent) {
        _events.emit(event)
    }

    override fun tryPublish(event: AppEvent): Boolean {
        return _events.tryEmit(event)
    }
}
