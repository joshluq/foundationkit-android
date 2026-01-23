package es.joshluq.foundationkit.map

interface Mappable

inline fun <R : Mappable, T> R.map(action: R.() -> T): T = action(this)

