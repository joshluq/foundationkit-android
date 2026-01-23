package es.joshluq.foundationkit.map

import org.junit.Assert.assertEquals
import org.junit.Test

class MappableTest {

    private data class TestMappable(val id: Int, val name: String) : Mappable

    @Test
    fun `map should transform Mappable to another object`() {
        val source = TestMappable(1, "Test")
        
        val result = source.map {
            "ID: $id, Name: $name"
        }

        assertEquals("ID: 1, Name: Test", result)
    }

    @Test
    fun `map should allow access to Mappable properties`() {
        val source = TestMappable(42, "Foundation")
        
        val result = source.map { id }
        
        assertEquals(42, result)
    }
}
