package es.joshluq.foundationkit.showcase

import es.joshluq.foundationkit.coroutines.throttleFirst
import es.joshluq.foundationkit.error.AppError
import es.joshluq.foundationkit.error.toTextProvider
import es.joshluq.foundationkit.testing.coroutines.MainDispatcherRule
import es.joshluq.foundationkit.testing.coroutines.TestDispatcherProvider
import es.joshluq.foundationkit.text.TextProvider
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

/**
 * Consumer-driven unit tests validating FoundationKit components from the client app perspective.
 */
class ExampleUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `consumer can instantiate TestDispatcherProvider and execute virtual time tests`() = runTest {
        val testDispatcherProvider = TestDispatcherProvider()
        assertNotNull(testDispatcherProvider.main)
    }

    @Test
    fun `consumer can use throttleFirst on Flow`() = runTest {
        val flow = flowOf(1, 2, 3)
        val throttled = flow.throttleFirst(1_000L).toList()
        assertEquals(listOf(1), throttled)
    }

    @Test
    fun `consumer can map AppError to TextProvider`() {
        val error: AppError = AppError.NoInternet()
        val textProvider = error.toTextProvider()
        assertEquals("No internet connection", (textProvider as TextProvider.Dynamic).value)
    }
}
