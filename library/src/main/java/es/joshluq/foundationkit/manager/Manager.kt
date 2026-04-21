package es.joshluq.foundationkit.manager

abstract class Manager<T : ManagerConfig> {

    protected lateinit var config: T

}

interface ManagerBuilder<T : ManagerConfig> {

    fun build(config: T): Manager<T>

}