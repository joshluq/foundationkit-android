package es.joshluq.foundationkit.storage

import es.joshluq.foundationkit.provider.StorageProvider
import java.util.concurrent.ConcurrentHashMap

/**
 * Implementation of [StorageProvider] that uses an in-memory cache.
 */
class CacheStorageProvider : StorageProvider {
    private val cache = ConcurrentHashMap<String, Any>()

    override suspend fun <T : Any> save(key: String, value: T, type: Class<T>) {
        cache[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> read(key: String, type: Class<T>): T? {
        val value = cache[key]
        return if (type.isInstance(value)) {
            value as T
        } else {
            null
        }
    }

    override suspend fun delete(key: String) {
        cache.remove(key)
    }

    override suspend fun clear() {
        cache.clear()
    }
}
