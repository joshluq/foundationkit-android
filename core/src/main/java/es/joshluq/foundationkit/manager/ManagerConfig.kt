package es.joshluq.foundationkit.manager

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
