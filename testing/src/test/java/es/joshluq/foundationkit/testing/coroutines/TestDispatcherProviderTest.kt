package es.joshluq.foundationkit.testing.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class TestDispatcherProviderTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `testDispatcherProvider routes all dispatchers to testDispatcher`() {
        val testDispatcher = StandardTestDispatcher()
        val provider = TestDispatcherProvider(testDispatcher)

        assertEquals(testDispatcher, provider.main)
        assertEquals(testDispatcher, provider.io)
        assertEquals(testDispatcher, provider.default)
        assertEquals(testDispatcher, provider.unconfined)
    }

    @Test
    fun `mainDispatcherRule configures Dispatchers Main`() {
        assertNotNull(Dispatchers.Main)
    }
}
