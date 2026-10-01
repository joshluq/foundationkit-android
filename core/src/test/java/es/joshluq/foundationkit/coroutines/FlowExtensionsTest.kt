package es.joshluq.foundationkit.coroutines

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class FlowExtensionsTest {

    @Test
    fun `throttleFirst throws exception for negative window`() {
        assertThrows(IllegalArgumentException::class.java) {
            flow { emit(1) }.throttleFirst(-1L)
        }
    }

    @Test
    fun `throttleFirst emits first element and ignores subsequent elements within same window`() = runTest {
        val upstream = flow {
            emit(1)
            emit(2)
            emit(3)
        }

        val result = upstream.throttleFirst(1_000L).toList()

        assertEquals(listOf(1), result)
    }

    @Test
    fun `throttleFirst with zero window emits all elements`() = runTest {
        val upstream = flow {
            emit(1)
            emit(2)
            emit(3)
        }

        val result = upstream.throttleFirst(0L).toList()

        assertEquals(listOf(1, 2, 3), result)
    }
}
