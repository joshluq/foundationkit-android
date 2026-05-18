package es.joshluq.foundationkit.provider

/**
 * Interface for storage providers. Implement this interface to provide data persistence
 * services (e.g., SharedPreferences, Room, DataStore).
 */
interface StorageProvider : Provider {
    /**
     * Saves the given value with the specified key.
     *
     * @param key The key to associate the value with.
     * @param value The value to be saved.
     * @param type The class type of the value.
     */
    suspend fun <T : Any> save(key: String, value: T, type: Class<T>)

    /**
     * Reads the value associated with the specified key.
     *
     * @param key The key to read the value from.
     * @param type The class type of the value.
     * @return The value associated with the key, or null if not found.
     */
    suspend fun <T : Any> read(key: String, type: Class<T>): T?

    /**
     * Deletes the value associated with the specified key.
     *
     * @param key The key of the value to be deleted.
     */
    suspend fun delete(key: String)

    /**
     * Clears all data stored by the provider.
     */
    suspend fun clear()
}

/**
 * Saves the given value with the specified key using reified type.
 *
 * @param key The key to associate the value with.
 * @param value The value to be saved.
 */
suspend inline fun <reified T : Any> StorageProvider.save(key: String, value: T) {
    save(key, value, T::class.java)
}

/**
 * Reads the value associated with the specified key using reified type.
 *
 * @param key The key to read the value from.
 * @return The value associated with the key, or null if not found.
 */
suspend inline fun <reified T : Any> StorageProvider.read(key: String): T? {
    return read(key, T::class.java)
}
