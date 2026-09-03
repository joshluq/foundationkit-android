package es.joshluq.foundationkit.log

import android.util.Log

/**
 * Default values and providers for Logging on Android.
 */
object LoggerDefaults {
    /**
     * Provides the default Android implementation of [LogProvider].
     *
     * @param minLogLevel Minimum level to log.
     * @param tagPrefix Global prefix for all tags (e.g., "LoggerKit").
     * @param showThread Whether to include the thread name in the message.
     * @param useEmojis Whether to prepend an emoji representing the log level.
     */
    fun defaultLogProvider(
        minLogLevel: LogLevel = LogLevel.VERBOSE,
        tagPrefix: String = "LoggerKit",
        showThread: Boolean = true,
        useEmojis: Boolean = true
    ): LogProvider = AndroidLogProvider(minLogLevel, tagPrefix, showThread, useEmojis)
}

/**
 * Android-specific implementation of [LogProvider] with decoration support.
 */
private class AndroidLogProvider(
    override val minLogLevel: LogLevel,
    private val tagPrefix: String,
    private val showThread: Boolean,
    private val useEmojis: Boolean
) : LogProvider {
    override fun log(priority: LogLevel, tag: String, message: String, throwable: Throwable?) {
        if (priority.priority < minLogLevel.priority) return

        val decoratedTag = if (tagPrefix.isNotEmpty()) "$tagPrefix [$tag]" else tag
        val decoratedMessage = decorateMessage(priority, message)

        when (priority) {
            LogLevel.VERBOSE -> Log.v(decoratedTag, decoratedMessage, throwable)
            LogLevel.DEBUG -> Log.d(decoratedTag, decoratedMessage, throwable)
            LogLevel.INFO -> Log.i(decoratedTag, decoratedMessage, throwable)
            LogLevel.WARN -> Log.w(decoratedTag, decoratedMessage, throwable)
            LogLevel.ERROR -> Log.e(decoratedTag, decoratedMessage, throwable)
            LogLevel.ASSERT -> Log.wtf(decoratedTag, decoratedMessage, throwable)
            LogLevel.NONE -> { /* No-op */ }
        }
    }

    private fun decorateMessage(priority: LogLevel, message: String): String {
        val threadInfo = if (showThread) "[${Thread.currentThread().name}] " else ""
        val emoji = if (useEmojis) "${priority.emoji} " else ""
        return "$emoji$threadInfo$message"
    }
}
