package es.joshluq.foundationkit.manager

import android.content.Context

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
 * @param C The type of configuration required by the manager to be built.
 * @param M The type of the manager to be built.
 */
interface ManagerBuilder<C : ManagerConfig, M : Manager<C>> {

    /**
     * Builds a new instance of [Manager] using the provided configuration.
     *
     * @param config The configuration for the new manager instance.
     * @return A configured [Manager] instance.
     */
    fun build(config: C): M
}

/**
 * Factory interface for managers that do NOT require an Android Context.
 *
 * @param M The type of the manager.
 * @param C The type of the configuration.
 * @param B The type of the configuration builder.
 */
interface ManagerFactory<M : Manager<C>, C : ManagerConfig, B : ConfigBuilder<C>> {

    /**
     * The internal builder used to create the manager instance from a configuration.
     */
    val builder: ManagerBuilder<C, M>

    /**
     * Creates a new builder instance.
     *
     * @return A new instance of the configuration builder.
     */
    fun createBuilder(): B

    /**
     * Entry point for DSL-based initialization without context.
     *
     * @param block The configuration DSL block.
     * @return A fully configured [Manager] instance.
     */
    fun build(block: B.() -> Unit): M {
        val dslBuilder = createBuilder()
        dslBuilder.block()
        val config = dslBuilder.build()
        return builder.build(config)
    }
}

/**
 * Factory interface for managers that REQUIRE an Android Context.
 *
 * Every Manager companion object should implement this interface to provide
 * a consistent entry point: `MyManager.build(context) { ... }`.
 *
 * @param M The type of the manager.
 * @param C The type of the configuration.
 * @param B The type of the configuration builder.
 */
interface ContextManagerFactory<M : Manager<C>, C : ManagerConfig, B : ContextConfigBuilder<C>> {

    /**
     * The internal builder used to create the manager instance from a configuration.
     */
    val builder: ManagerBuilder<C, M>

    /**
     * Creates a new builder instance.
     *
     * @param context The context for initialization.
     * @return A new instance of the configuration builder.
     */
    fun createBuilder(context: Context): B

    /**
     * Entry point for DSL-based initialization with context.
     *
     * @param context The context for initialization.
     * @param block The configuration DSL block.
     * @return A fully configured [Manager] instance.
     */
    fun build(context: Context, block: B.() -> Unit): M {
        val dslBuilder = createBuilder(context.toSafeContext())
        dslBuilder.block()
        val config = dslBuilder.build()
        return builder.build(config)
    }
}
