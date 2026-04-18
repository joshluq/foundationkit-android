package es.joshluq.foundationkit.provider

/**
 * Interface for serializer providers. Implement this interface to provide data serialization
 * and deserialization services (e.g., GSON, Kotlin Serialization, Moshi).
 */
interface SerializerProvider : Provider {

    /**
     * Serializes the given object to a string.
     *
     * @param value The object to be serialized.
     * @param type The class type of the value.
     * @return The serialized string.
     */
    fun <T : Any> serialize(value: T, type: Class<T>): String

    /**
     * Deserializes the given string to an object of the specified type.
     *
     * @param value The string to be deserialized.
     * @param type The class type of the value.
     * @return The deserialized object.
     */
    fun <T : Any> deserialize(value: String, type: Class<T>): T
}

/**
 * Serializes the given object to a string using reified type.
 *
 * @param value The object to be serialized.
 * @return The serialized string.
 */
inline fun <reified T : Any> SerializerProvider.serialize(value: T): String {
    return serialize(value, T::class.java)
}

/**
 * Deserializes the given string to an object of the specified type using reified type.
 *
 * @param value The string to be deserialized.
 * @return The deserialized object.
 */
inline fun <reified T : Any> SerializerProvider.deserialize(value: String): T {
    return deserialize(value, T::class.java)
}
