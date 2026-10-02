package es.joshluq.foundationkit.testing.event

import es.joshluq.foundationkit.event.AppEvent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeAppEventBusTest {
    private data class TestEvent(
        val message: String,
    ) : AppEvent

    private data class OtherEvent(
        val code: Int,
    ) : AppEvent

    @Test
    fun `publish records event in publishedEvents`() =
        runTest {
            val fakeBus = FakeAppEventBus()
            val event = TestEvent("hello")

            fakeBus.publish(event)

            assertEquals(listOf(event), fakeBus.publishedEvents)
            assertEquals(event, fakeBus.findEvent<TestEvent>())
            assertNull(fakeBus.findEvent<OtherEvent>())
        }

    @Test
    fun `tryPublish records event and returns true`() {
        val fakeBus = FakeAppEventBus()
        val event = OtherEvent(42)

        val success = fakeBus.tryPublish(event)

        assertTrue(success)
        assertEquals(listOf(event), fakeBus.filterEvents<OtherEvent>())
    }

    @Test
    fun `clear removes all recorded events`() =
        runTest {
            val fakeBus = FakeAppEventBus()
            fakeBus.publish(TestEvent("one"))
            fakeBus.publish(OtherEvent(2))

            fakeBus.clear()

            assertTrue(fakeBus.publishedEvents.isEmpty())
        }
}
