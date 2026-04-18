package es.joshluq.foundationkit.manager

abstract class Manager<T : ManagerConfig> {

    protected var isInitialized = false
    protected lateinit var config: T

    open fun initialize(config: T): Result<Unit> = runCatching {
        this.config = config
        isInitialized = true
        onInitialize()
    }

    protected open fun onInitialize() { }

    protected fun checkInitialization() {
        check(isInitialized) {
            "Manager ${this::class.simpleName} is not initialized. Call initialize() first."
        }
    }
}
