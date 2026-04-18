package es.joshluq.foundationkit.storage

import es.joshluq.foundationkit.provider.StorageProvider

/**
 * Implementation of [StorageProvider] that uses an in-memory cache.
 */
class CacheStorageProvider : StorageProvider {
    private val cache = mutableMapOf<String, Any>()

    override fun <T : Any> save(key: String, value: T, type: Class<T>) {
        cache[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> read(key: String, type: Class<T>): T? {
        val value = cache[key]
        return if (type.isInstance(value)) {
            value as T
        } else {
            null
        }
    }

    override fun delete(key: String) {
        cache.remove(key)
    }

    override fun clear() {
        cache.clear()
    }
}
