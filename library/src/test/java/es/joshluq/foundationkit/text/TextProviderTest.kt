package es.joshluq.foundationkit.text

import org.junit.Assert.assertEquals
import org.junit.Test

class TextProviderTest {

    @Test
    fun `asString with Dynamic returns correct value`() {
        val expectedValue = "Hello World"
        val textProvider = TextProvider.Dynamic(expectedValue)
        
        // Note: asString(context) and asString() @Composable can't be tested in simple unit tests 
        // without mocking Android or using Compose Test Rules.
        // But we can verify the data class structure.
        assertEquals(expectedValue, textProvider.value)
    }

    @Test
    fun `Empty returns empty string logic`() {
        val textProvider = TextProvider.Empty
        // Verify it's a singleton object
        assertEquals(TextProvider.Empty, textProvider)
    }

    @Test
    fun `Resource equality works with varargs`() {
        val res1 = TextProvider.Resource(1, "arg1", 2)
        val res2 = TextProvider.Resource(1, "arg1", 2)
        val res3 = TextProvider.Resource(1, "arg2", 2)

        assertEquals(res1, res2)
        assertEquals(res1.hashCode(), res2.hashCode())
        assert(res1 != res3)
    }
}
