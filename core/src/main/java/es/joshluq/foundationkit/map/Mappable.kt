package es.joshluq.foundationkit.map

/**
 * Marker interface representing an object capable of being mapped or transformed across architectural layers.
 *
 * Implement this interface on Data Transfer Objects (DTOs), entity schemas, or domain representations
 * to enable standardized mapping transformations using the [map] extension function.
 */
interface Mappable

/**
 * Transforms this [Mappable] instance into another representation of type [T] using the given [action] lambda.
 *
 * @param R The receiver type implementing [Mappable].
 * @param T The target mapped type.
 * @param action Lambda with receiver executing the layer mapping logic.
 * @return The mapped object of type [T].
 */
inline fun <R : Mappable, T> R.map(action: R.() -> T): T = action(this)
