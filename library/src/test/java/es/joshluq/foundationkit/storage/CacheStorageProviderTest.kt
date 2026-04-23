package es.joshluq.foundationkit.storage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CacheStorageProviderTest {

    private lateinit var storageProvider: CacheStorageProvider

    @Before
    fun setUp() {
        storageProvider = CacheStorageProvider()
    }

    @Test
    fun `save and read should work correctly`() {
        val key = "testKey"
        val value = "testValue"

        storageProvider.save(key, value, String::class.java)
        val result = storageProvider.read(key, String::class.java)

        assertEquals(value, result)
    }

    @Test
    fun `read should return null if key does not exist`() {
        val result = storageProvider.read("nonExistentKey", String::class.java)
        assertNull(result)
    }

    @Test
    fun `read should return null if type does not match`() {
        val key = "testKey"
        storageProvider.save(key, 123, Int::class.java)

        val result = storageProvider.read(key, String::class.java)
        assertNull(result)
    }

    @Test
    fun `delete should remove the value`() {
        val key = "testKey"
        storageProvider.save(key, "value", String::class.java)
        storageProvider.delete(key)

        val result = storageProvider.read(key, String::class.java)
        assertNull(result)
    }

    @Test
    fun `clear should remove all values`() {
        storageProvider.save("key1", "value1", String::class.java)
        storageProvider.save("key2", "value2", String::class.java)
        storageProvider.clear()

        assertNull(storageProvider.read("key1", String::class.java))
        assertNull(storageProvider.read("key2", String::class.java))
    }

    @Test
    fun `concurrency test - multiple threads saving and reading`() {
        val numberOfThreads = 100
        val iterationsPerThread = 100
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        for (i in 0 until numberOfThreads) {
            executorService.execute {
                try {
                    for (j in 0 until iterationsPerThread) {
                        val key = "key_${i}_${j}"
                        val value = "value_${i}_${j}"
                        storageProvider.save(key, value, String::class.java)
                        assertEquals(value, storageProvider.read(key, String::class.java))
                    }
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await(10, TimeUnit.SECONDS)
        executorService.shutdown()
    }
}
