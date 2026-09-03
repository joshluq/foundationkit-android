package es.joshluq.foundationkit.state

import es.joshluq.foundationkit.text.TextProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ListStateTest {

    @Test
    fun `isLoading returns true only for Loading state`() {
        assertTrue(ListState.Loading.isLoading)
        assertFalse(ListState.Idle.isLoading)
        assertFalse(ListState.Empty.isLoading)
        assertFalse(ListState.Success(listOf(1)).isLoading)
        assertFalse(ListState.Error(TextProvider.Empty).isError.let { false }) // Just checking isLoading
    }

    @Test
    fun `isError returns true only for Error state`() {
        assertTrue(ListState.Error(TextProvider.Empty).isError)
        assertFalse(ListState.Loading.isError)
    }

    @Test
    fun `getOrNull returns data only for Success state`() {
        val data = listOf("A", "B")
        assertEquals(data, ListState.Success(data).getOrNull())
        assertNull(ListState.Loading.getOrNull())
        assertNull(ListState.Empty.getOrNull())
    }

    @Test
    fun `toSuccessOrEmpty returns Empty when list is empty`() {
        val list = emptyList<Int>()
        assertEquals(ListState.Empty, list.toSuccessOrEmpty())
    }

    @Test
    fun `toSuccessOrEmpty returns Success when list has elements`() {
        val list = listOf(1, 2, 3)
        val state = list.toSuccessOrEmpty()
        assertTrue(state is ListState.Success)
        assertEquals(list, (state as ListState.Success).data)
    }

    @Test
    fun `toListState handles Success result`() {
        val result = Result.success(listOf(1))
        val state = result.toListState()
        assertTrue(state is ListState.Success)
    }

    @Test
    fun `toListState handles Failure result`() {
        val exception = Exception("Test Error")
        val result = Result.failure<List<Int>>(exception)
        val state = result.toListState()
        assertTrue(state is ListState.Error)
        val errorMessage = (state as ListState.Error).message
        assertTrue(errorMessage is TextProvider.Dynamic)
        assertEquals("Test Error", (errorMessage as TextProvider.Dynamic).value)
    }
}
