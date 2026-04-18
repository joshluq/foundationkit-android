package es.joshluq.foundationkit.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import es.joshluq.foundationkit.provider.SerializerProvider
import es.joshluq.foundationkit.provider.StorageProvider

/**
 * Implementation of [StorageProvider] that uses [SharedPreferences].
 *
 * @param sharedPreferences The [SharedPreferences] instance to use.
 * @param serializer The [SerializerProvider] to use for complex objects.
 */
class SharedPreferencesStorageProvider(
    private val sharedPreferences: SharedPreferences,
    private val serializer: SerializerProvider
) : StorageProvider {

    override fun <T : Any> save(key: String, value: T, type: Class<T>) {
        sharedPreferences.edit {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                else -> {
                    val serializedValue = serializer.serialize(value, type)
                    putString(key, serializedValue)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> read(key: String, type: Class<T>): T? {
        if (!sharedPreferences.contains(key)) return null

        return when (type) {
            String::class.java -> sharedPreferences.getString(key, null) as? T
            Int::class.javaObjectType, Int::class.java -> sharedPreferences.getInt(key, 0) as? T
            Long::class.javaObjectType, Long::class.java -> sharedPreferences.getLong(key, 0L) as? T
            Float::class.javaObjectType, Float::class.java -> sharedPreferences.getFloat(key, 0f) as? T
            Boolean::class.javaObjectType, Boolean::class.java -> sharedPreferences.getBoolean(key, false) as? T
            else -> {
                val serializedValue = sharedPreferences.getString(key, null)
                serializedValue?.let { serializer.deserialize(it, type) }
            }
        }
    }

    override fun delete(key: String) {
        sharedPreferences.edit { remove(key) }
    }

    override fun clear() {
        sharedPreferences.edit { clear() }
    }
}
