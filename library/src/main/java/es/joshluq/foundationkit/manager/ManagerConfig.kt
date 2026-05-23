package es.joshluq.foundationkit.manager

import android.content.Context

/**
 * Base interface for manager configurations.
 *
 * All specific manager configuration interfaces should extend this.
 */
interface ManagerConfig

/**
 * Base interface for manager configuration builders.
 *
 * @param C The type of configuration this builder produces.
 */
interface ConfigBuilder<C : ManagerConfig> {
    /**
     * Builds the final configuration instance.
     *
     * @return A configured [ManagerConfig] instance.
     */
    fun build(): C
}

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
