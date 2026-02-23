package es.joshluq.foundationkit.coroutines

import es.joshluq.foundationkit.log.LogLevel
import es.joshluq.foundationkit.log.LogProvider
import es.joshluq.foundationkit.log.Loggerkit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineExtensionsTest {

    @Test
    fun `launchSafe catches exception and calls onError`() = runTest {
        var errorCaught: Throwable? = null
        val exception = RuntimeException("Test Exception")

        launchSafe(
            onError = { errorCaught = it }
        ) {
            throw exception
        }.join()

        assertEquals(exception, errorCaught)
    }

    @Test
    fun `launchSafe logs error when logger is provided`() = runTest {
        val testProvider = TestLogProvider()
        val logger = Loggerkit.Builder().setProvider(testProvider).build()
        val exception = RuntimeException("Logging Test")

        launchSafe(
            logger = logger
        ) {
            throw exception
        }.join()

        assertEquals(LogLevel.ERROR, testProvider.lastPriority)
        assertEquals("launchSafe", testProvider.lastTag)
        assertTrue(testProvider.lastMessage?.contains("Logging Test") == true)
    }

    @Test
    fun `safeRun returns success Result`() = runTest {
        val result = safeRun {
            "Success"
        }

        assertTrue(result.isSuccess)
        assertEquals("Success", result.getOrNull())
    }

    @Test
    fun `safeRun returns failure Result and logs error`() = runTest {
        val testProvider = TestLogProvider()
        val logger = Loggerkit.Builder().setProvider(testProvider).build()
        val exception = RuntimeException("SafeRunner Test")

        val result = safeRun(logger = logger) {
            throw exception
        }

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        assertEquals(LogLevel.ERROR, testProvider.lastPriority)
        assertEquals("SafeRunner", testProvider.lastTag)
    }

    private class TestLogProvider : LogProvider {
        override val minLogLevel: LogLevel = LogLevel.VERBOSE
        var lastPriority: LogLevel? = null
        var lastTag: String? = null
        var lastMessage: String? = null

        override fun log(priority: LogLevel, tag: String, message: String, throwable: Throwable?) {
            lastPriority = priority
            lastTag = tag
            lastMessage = message
        }
    }
}
