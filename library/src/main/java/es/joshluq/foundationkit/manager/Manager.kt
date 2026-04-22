package es.joshluq.foundationkit.manager

abstract class Manager<T : ManagerConfig> {

    @Volatile
    protected lateinit var config: T

    /**
     * Internal check to ensure config is initialized before use.
     */
    protected fun isConfigInitialized(): Boolean = ::config.isInitialized

}

interface ManagerBuilder<T : ManagerConfig> {

    fun build(config: T): Manager<T>

}