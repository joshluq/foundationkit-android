package es.joshluq.foundationkit.storage

import android.content.SharedPreferences
import es.joshluq.foundationkit.provider.SerializerProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SharedPreferencesStorageProviderTest {

    private lateinit var fakeSharedPreferences: FakeSharedPreferences
    private lateinit var fakeSerializer: FakeSerializerProvider
    private lateinit var storageProvider: SharedPreferencesStorageProvider

    @Before
    fun setUp() {
        fakeSharedPreferences = FakeSharedPreferences()
        fakeSerializer = FakeSerializerProvider()
        storageProvider = SharedPreferencesStorageProvider(fakeSharedPreferences, fakeSerializer)
    }

    @Test
    fun `save and read String should work`() {
        val key = "testKey"
        val value = "testValue"
        storageProvider.save(key, value, String::class.java)
        assertEquals(value, storageProvider.read(key, String::class.java))
    }

    @Test
    fun `save and read Int should work`() {
        val key = "testKey"
        val value = 42
        storageProvider.save(key, value, Int::class.java)
        assertEquals(value, storageProvider.read(key, Int::class.java))
    }

    @Test
    fun `save and read Long should work`() {
        val key = "testKey"
        val value = 123456789L
        storageProvider.save(key, value, Long::class.java)
        assertEquals(value, storageProvider.read(key, Long::class.java))
    }

    @Test
    fun `save and read Float should work`() {
        val key = "testKey"
        val value = 3.14f
        storageProvider.save(key, value, Float::class.java)
        assertEquals(value, storageProvider.read(key, Float::class.java))
    }

    @Test
    fun `save and read Boolean should work`() {
        val key = "testKey"
        val value = true
        storageProvider.save(key, value, Boolean::class.java)
        assertEquals(value, storageProvider.read(key, Boolean::class.java))
    }

    @Test
    fun `save and read complex object should use serializer`() {
        val key = "complexKey"
        val value = ComplexObject("John", 30)
        
        storageProvider.save(key, value, ComplexObject::class.java)
        
        // Check if it was saved as String in SharedPreferences
        assertTrue(fakeSharedPreferences.contains(key))
        val rawValue = fakeSharedPreferences.getString(key, null)
        assertEquals("ComplexObject(name=John, age=30)", rawValue)
        
        // Read back
        val result = storageProvider.read(key, ComplexObject::class.java)
        assertEquals(value, result)
    }

    @Test
    fun `read non-existent key should return null`() {
        assertNull(storageProvider.read("unknown", String::class.java))
    }

    @Test
    fun `delete should remove the key`() {
        val key = "testKey"
        storageProvider.save(key, "value", String::class.java)
        assertTrue(fakeSharedPreferences.contains(key))
        
        storageProvider.delete(key)
        assertFalse(fakeSharedPreferences.contains(key))
    }

    @Test
    fun `clear should remove all keys`() {
        storageProvider.save("key1", "val1", String::class.java)
        storageProvider.save("key2", "val2", String::class.java)
        
        storageProvider.clear()
        
        assertFalse(fakeSharedPreferences.contains("key1"))
        assertFalse(fakeSharedPreferences.contains("key2"))
    }

    private data class ComplexObject(val name: String, val age: Int)

    private class FakeSerializerProvider : SerializerProvider {
        override fun <T : Any> serialize(value: T, type: Class<T>): String {
            return value.toString()
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> deserialize(value: String, type: Class<T>): T {
            if (type == ComplexObject::class.java) {
                // Mock parsing: ComplexObject(name=John, age=30)
                val name = value.substringAfter("name=").substringBefore(",")
                val age = value.substringAfter("age=").substringBefore(")").toInt()
                return ComplexObject(name, age) as T
            }
            throw IllegalArgumentException("Unsupported type")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private class FakeSharedPreferences : SharedPreferences {
        private val data = mutableMapOf<String, Any?>()

        override fun getAll(): Map<String, *> = data
        override fun getString(key: String, defValue: String?): String? = data[key] as? String ?: defValue
        override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? = data[key] as? Set<String> ?: defValues
        override fun getInt(key: String, defValue: Int): Int = data[key] as? Int ?: defValue
        override fun getLong(key: String, defValue: Long): Long = data[key] as? Long ?: defValue
        override fun getFloat(key: String, defValue: Float): Float = data[key] as? Float ?: defValue
        override fun getBoolean(key: String, defValue: Boolean): Boolean = data[key] as? Boolean ?: defValue
        override fun contains(key: String): Boolean = data.containsKey(key)
        override fun edit(): SharedPreferences.Editor = FakeEditor(data)
        
        override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}
        override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}

        private class FakeEditor(private val data: MutableMap<String, Any?>) : SharedPreferences.Editor {
            private val tempChanges = mutableMapOf<String, Any?>()
            private var clearCalled = false

            override fun putString(key: String, value: String?): SharedPreferences.Editor { tempChanges[key] = value; return this }
            override fun putStringSet(key: String, values: Set<String>?): SharedPreferences.Editor { tempChanges[key] = values; return this }
            override fun putInt(key: String, value: Int): SharedPreferences.Editor { tempChanges[key] = value; return this }
            override fun putLong(key: String, value: Long): SharedPreferences.Editor { tempChanges[key] = value; return this }
            override fun putFloat(key: String, value: Float): SharedPreferences.Editor { tempChanges[key] = value; return this }
            override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor { tempChanges[key] = value; return this }
            override fun remove(key: String): SharedPreferences.Editor { tempChanges[key] = null; return this }
            override fun clear(): SharedPreferences.Editor { clearCalled = true; return this }
            
            override fun commit(): Boolean {
                if (clearCalled) data.clear()
                tempChanges.forEach { (k, v) -> if (v == null) data.remove(k) else data[k] = v }
                return true
            }
            
            override fun apply() { commit() }
        }
    }
}
