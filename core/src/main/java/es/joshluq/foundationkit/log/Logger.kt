package es.joshluq.foundationkit.log

/**
 * Log levels for filtering output.
 */
enum class LogLevel(val priority: Int, val emoji: String) {
    VERBOSE(2, "📝"),
    DEBUG(3, "🔍"),
    INFO(4, "ℹ️"),
    WARN(5, "⚠️"),
    ERROR(6, "🚨"),
    ASSERT(7, "💣"),
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
interface LoggerKit {
    fun v(tag: String, message: String, throwable: Throwable? = null)
    fun d(tag: String, message: String, throwable: Throwable? = null)
    fun i(tag: String, message: String, throwable: Throwable? = null)
    fun w(tag: String, message: String, throwable: Throwable? = null)
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun wtf(tag: String, message: String, throwable: Throwable? = null)

    /**
     * Builder for creating [LoggerKit] instances.
     */
    class Builder {
        private val providers = mutableListOf<LogProvider>()

        fun addProvider(provider: LogProvider) = apply {
            this.providers.add(provider)
        }

        fun build(): LoggerKit {
            return FoundationLogger(providers.toList())
        }
    }
}

/**
 * Implementation of [LoggerKit] that routes logs to multiple [LogProvider] instances.
 */
internal class FoundationLogger(private val providers: List<LogProvider>) : LoggerKit {
    override fun v(tag: String, message: String, throwable: Throwable?) =
        providers.forEach { it.log(LogLevel.VERBOSE, tag, message, throwable) }

    override fun d(tag: String, message: String, throwable: Throwable?) =
        providers.forEach { it.log(LogLevel.DEBUG, tag, message, throwable) }

    override fun i(tag: String, message: String, throwable: Throwable?) =
        providers.forEach { it.log(LogLevel.INFO, tag, message, throwable) }

    override fun w(tag: String, message: String, throwable: Throwable?) =
        providers.forEach { it.log(LogLevel.WARN, tag, message, throwable) }

    override fun e(tag: String, message: String, throwable: Throwable?) =
        providers.forEach { it.log(LogLevel.ERROR, tag, message, throwable) }

    override fun wtf(tag: String, message: String, throwable: Throwable?) =
        providers.forEach { it.log(LogLevel.ASSERT, tag, message, throwable) }
}
