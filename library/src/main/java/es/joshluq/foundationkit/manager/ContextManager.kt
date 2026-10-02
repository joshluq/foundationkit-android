package es.joshluq.foundationkit.manager

import android.content.Context

/**
 * Specialized builder for managers that require an Android [Context].
 *
 * @param C The type of configuration this builder produces.
 */
interface ContextConfigBuilder<C : ManagerConfig> : ConfigBuilder<C> {
    /**
     * The context used for initialization.
     * Implementations should use [toSafeContext] in the factory to ensure memory safety.
     */
    val context: Context
}

/**
 * Extension to ensure we always use the application context to avoid memory leaks.
 */
fun Context.toSafeContext(): Context = this.applicationContext

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
    fun build(
        context: Context,
        block: B.() -> Unit,
    ): M {
        val dslBuilder = createBuilder(context.toSafeContext())
        dslBuilder.block()
        val config = dslBuilder.build()
        return builder.build(config)
    }
}
