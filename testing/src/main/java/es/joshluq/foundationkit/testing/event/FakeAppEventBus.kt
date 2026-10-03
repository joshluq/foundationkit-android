package es.joshluq.foundationkit.testing.event

import es.joshluq.foundationkit.event.AppEvent
import es.joshluq.foundationkit.event.AppEventBus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Fake implementation of [AppEventBus] for unit testing.
 *
 * Captures all published events in [publishedEvents] for assertions.
 */
class FakeAppEventBus : AppEventBus {
    private val _events = MutableSharedFlow<AppEvent>(extraBufferCapacity = 64)
    override val events: Flow<AppEvent> = _events.asSharedFlow()

    private val _publishedEvents = mutableListOf<AppEvent>()
    val publishedEvents: List<AppEvent> get() = _publishedEvents.toList()

    override suspend fun publish(event: AppEvent) {
        _publishedEvents.add(event)
        _events.emit(event)
    }

    override fun tryPublish(event: AppEvent): Boolean {
        _publishedEvents.add(event)
        return _events.tryEmit(event)
    }

    /**
     * Clears all recorded events from [publishedEvents].
     */
    fun clear() {
        _publishedEvents.clear()
    }

    /**
     * Finds the first event of type [T], or null if none was published.
     */
    inline fun <reified T : AppEvent> findEvent(): T? = publishedEvents.filterIsInstance<T>().firstOrNull()

    /**
     * Filters all published events of type [T].
     */
    inline fun <reified T : AppEvent> filterEvents(): List<T> = publishedEvents.filterIsInstance<T>()
}
