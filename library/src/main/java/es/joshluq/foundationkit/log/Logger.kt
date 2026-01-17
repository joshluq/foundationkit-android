package es.joshluq.foundationkit.log

import android.util.Log

/**
 * Log levels for filtering output.
 */
enum class LogLevel(val priority: Int, val emoji: String) {
    VERBOSE(Log.VERBOSE, "📝"),
    DEBUG(Log.DEBUG, "🔍"),
    INFO(Log.INFO, "ℹ️"),
    WARN(Log.WARN, "⚠️"),
    ERROR(Log.ERROR, "🚨"),
    ASSERT(Log.ASSERT, "💣"),
    NONE(Int.MAX_VALUE, "")
}

/**
 * Interface for providing log implementations.
 */
interface LogProvider {
    val minLogLevel: LogLevel
    fun log(priority: LogLevel, tag: String, message: String, throwable: Throwable? = null)
}

/**
 * Interface for logging operations, following Dependency Inversion.
 */
interface Loggerkit {
    fun v(tag: String, message: String, throwable: Throwable? = null)
    fun d(tag: String, message: String, throwable: Throwable? = null)
    fun i(tag: String, message: String, throwable: Throwable? = null)
    fun w(tag: String, message: String, throwable: Throwable? = null)
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun wtf(tag: String, message: String, throwable: Throwable? = null)

    /**
     * Builder for creating [Loggerkit] instances.
     */
    class Builder {
        private var provider: LogProvider = LoggerDefaults.defaultLogProvider()

        fun setProvider(provider: LogProvider) = apply {
            this.provider = provider
        }

        fun build(): Loggerkit = FoundationLogger(provider)
    }
}

/**
 * Default implementation of [Loggerkit].
 */
private class FoundationLogger(private val provider: LogProvider) : Loggerkit {
    override fun v(tag: String, message: String, throwable: Throwable?) =
        provider.log(LogLevel.VERBOSE, tag, message, throwable)

    override fun d(tag: String, message: String, throwable: Throwable?) =
        provider.log(LogLevel.DEBUG, tag, message, throwable)

    override fun i(tag: String, message: String, throwable: Throwable?) =
        provider.log(LogLevel.INFO, tag, message, throwable)

    override fun w(tag: String, message: String, throwable: Throwable?) =
        provider.log(LogLevel.WARN, tag, message, throwable)

    override fun e(tag: String, message: String, throwable: Throwable?) =
        provider.log(LogLevel.ERROR, tag, message, throwable)

    override fun wtf(tag: String, message: String, throwable: Throwable?) =
        provider.log(LogLevel.ASSERT, tag, message, throwable)
}

/**
 * Default values and providers for Logging, similar to Compose Defaults pattern.
 */
object LoggerDefaults {
    /**
     * Provides the default Android implementation of [LogProvider].
     *
     * @param minLogLevel Minimum level to log.
     * @param tagPrefix Global prefix for all tags (e.g., "Loggerkit").
     * @param showThread Whether to include the thread name in the message.
     * @param useEmojis Whether to prepend an emoji representing the log level.
     */
    fun defaultLogProvider(
        minLogLevel: LogLevel = LogLevel.VERBOSE,
        tagPrefix: String = "Loggerkit",
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
        if (priority.priority >= minLogLevel.priority) {
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
    }

    private fun decorateMessage(priority: LogLevel, message: String): String {
        val threadInfo = if (showThread) "[${Thread.currentThread().name}] " else ""
        val emoji = if (useEmojis) "${priority.emoji} " else ""
        return "$emoji$threadInfo$message"
    }
}
