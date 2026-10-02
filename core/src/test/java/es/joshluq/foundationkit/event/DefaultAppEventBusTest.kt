package es.joshluq.foundationkit.event

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultAppEventBusTest {

    private sealed interface SampleEvent : AppEvent {
        data class UserLoggedIn(val userId: String) : SampleEvent
        data class UserLoggedOut(val reason: String) : SampleEvent
        data object TokenExpired : SampleEvent
    }

    @Test
    fun `publish delivers event to subscriber`() = runTest {
        val eventBus = DefaultAppEventBus()
        val receivedEvents = mutableListOf<AppEvent>()

        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            eventBus.events.toList(receivedEvents)
        }

        val event = SampleEvent.UserLoggedIn("user-123")
        eventBus.publish(event)

        assertEquals(listOf(event), receivedEvents)
        job.cancel()
    }

    @Test
    fun `tryPublish emits event synchronously`() = runTest {
        val eventBus = DefaultAppEventBus()
        val receivedEvents = mutableListOf<AppEvent>()

        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            eventBus.events.toList(receivedEvents)
        }

        val event = SampleEvent.TokenExpired
        val emitted = eventBus.tryPublish(event)

        assertTrue(emitted)
        assertEquals(listOf(event), receivedEvents)
        job.cancel()
    }

    @Test
    fun `subscribe with reified type filters only matching events`() = runTest {
        val eventBus = DefaultAppEventBus()
        val receivedLogins = mutableListOf<SampleEvent.UserLoggedIn>()

        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            eventBus.subscribe<SampleEvent.UserLoggedIn>().toList(receivedLogins)
        }

        eventBus.publish(SampleEvent.TokenExpired)
        eventBus.publish(SampleEvent.UserLoggedIn("user-456"))
        eventBus.publish(SampleEvent.UserLoggedOut("timeout"))
        eventBus.publish(SampleEvent.UserLoggedIn("user-789"))

        assertEquals(
            listOf(
                SampleEvent.UserLoggedIn("user-456"),
                SampleEvent.UserLoggedIn("user-789")
            ),
            receivedLogins
        )
        job.cancel()
    }

    @Test
    fun `multiple subscribers receive published events`() = runTest {
        val eventBus = DefaultAppEventBus()
        val subscriber1 = mutableListOf<AppEvent>()
        val subscriber2 = mutableListOf<AppEvent>()

        val job1 = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            eventBus.events.toList(subscriber1)
        }
        val job2 = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            eventBus.events.toList(subscriber2)
        }

        val event = SampleEvent.TokenExpired
        eventBus.publish(event)

        assertEquals(listOf(event), subscriber1)
        assertEquals(listOf(event), subscriber2)

        job1.cancel()
        job2.cancel()
    }
}
