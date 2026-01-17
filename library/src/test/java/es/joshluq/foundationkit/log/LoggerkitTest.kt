package es.joshluq.foundationkit.log

import org.junit.Assert.assertEquals
import org.junit.Test

class LoggerkitTest {

    @Test
    fun `Loggerkit with custom provider should call log method`() {
        val mockProvider = TestLogProvider()
        val logger = Loggerkit.Builder()
            .setProvider(mockProvider)
            .build()

        val tag = "TestTag"
        val message = "Test Message"
        
        logger.d(tag, message)

        assertEquals(LogLevel.DEBUG, mockProvider.lastPriority)
        assertEquals(tag, mockProvider.lastTag)
        assertEquals(message, mockProvider.lastMessage)
    }

    @Test
    fun `Loggerkit should trigger all log levels`() {
        val mockProvider = TestLogProvider()
        val logger = Loggerkit.Builder()
            .setProvider(mockProvider)
            .build()

        logger.v("T", "M")
        assertEquals(LogLevel.VERBOSE, mockProvider.lastPriority)
        
        logger.d("T", "M")
        assertEquals(LogLevel.DEBUG, mockProvider.lastPriority)
        
        logger.i("T", "M")
        assertEquals(LogLevel.INFO, mockProvider.lastPriority)
        
        logger.w("T", "M")
        assertEquals(LogLevel.WARN, mockProvider.lastPriority)
        
        logger.e("T", "M")
        assertEquals(LogLevel.ERROR, mockProvider.lastPriority)
        
        logger.wtf("T", "M")
        assertEquals(LogLevel.ASSERT, mockProvider.lastPriority)
    }

    /**
     * Test provider that captures the last log call.
     * We use this instead of MockK because MockK might not be available
     * in the environment or to avoid extra dependencies in the core.
     */
    private class TestLogProvider : LogProvider {
        override val minLogLevel: LogLevel = LogLevel.VERBOSE
        var lastPriority: LogLevel? = null
        var lastTag: String? = null
        var lastMessage: String? = null
        var lastThrowable: Throwable? = null

        override fun log(priority: LogLevel, tag: String, message: String, throwable: Throwable?) {
            lastPriority = priority
            lastTag = tag
            lastMessage = message
            lastThrowable = throwable
        }
    }
}
