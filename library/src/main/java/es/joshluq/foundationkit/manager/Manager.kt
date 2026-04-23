package es.joshluq.foundationkit.manager

/**
 * Base class for all managers in the system.
 *
 * A manager is responsible for coordinating a specific domain or functionality.
 * It is configured with a [ManagerConfig].
 *
 * @param T The type of configuration required by this manager.
 */
abstract class Manager<T : ManagerConfig> {

    /**
     * The configuration for this manager.
     * It is marked as [Volatile] and `lateinit` to allow for asynchronous initialization.
     */
    @Volatile
    protected lateinit var config: T

    /**
     * Internal check to ensure config is initialized before use.
     */
    protected fun isConfigInitialized(): Boolean = ::config.isInitialized
}

/**
 * Interface for building instances of [Manager].
 *
 * @param T The type of configuration required by the manager to be built.
 */
interface ManagerBuilder<T : ManagerConfig> {

    /**
     * Builds a new instance of [Manager] using the provided configuration.
     *
     * @param config The configuration for the new manager instance.
     * @return A configured [Manager] instance.
     */
    fun build(config: T): Manager<T>
}
