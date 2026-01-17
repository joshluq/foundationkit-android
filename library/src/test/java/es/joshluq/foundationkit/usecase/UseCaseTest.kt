package es.joshluq.foundationkit.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UseCaseTest {

    private data class TestInput(val value: String) : UseCaseInput
    private data class TestOutput(val result: String) : UseCaseOutput

    @Test
    fun `UseCase should return success result`() = runTest {
        val useCase = object : UseCase<TestInput, TestOutput> {
            override suspend fun invoke(input: TestInput): Result<TestOutput> {
                return Result.success(TestOutput("Processed ${input.value}"))
            }
        }

        val result = useCase(TestInput("test"))
        
        assertTrue(result.isSuccess)
        assertEquals("Processed test", result.getOrNull()?.result)
    }

    @Test
    fun `FlowUseCase should emit results`() = runTest {
        val useCase = object : FlowUseCase<TestInput, TestOutput> {
            override fun invoke(input: TestInput) = flowOf(
                Result.success(TestOutput("Emitted ${input.value}"))
            )
        }

        val result = useCase(TestInput("test")).first()

        assertTrue(result.isSuccess)
        assertEquals("Emitted test", result.getOrNull()?.result)
    }
}
