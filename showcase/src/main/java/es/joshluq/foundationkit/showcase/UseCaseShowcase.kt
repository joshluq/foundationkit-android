package es.joshluq.foundationkit.showcase

import es.joshluq.foundationkit.usecase.FlowUseCase
import es.joshluq.foundationkit.usecase.NoneInput
import es.joshluq.foundationkit.usecase.UseCase
import es.joshluq.foundationkit.usecase.UseCaseInput
import es.joshluq.foundationkit.usecase.UseCaseOutput
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Example of a standard UseCase.
 */
data class GetGreetingInput(val name: String) : UseCaseInput
data class GreetingOutput(val message: String) : UseCaseOutput

class GetGreetingUseCase : UseCase<GetGreetingInput, GreetingOutput> {
    override suspend fun invoke(input: GetGreetingInput): Result<GreetingOutput> {
        delay(500) // Simulate work
        return if (input.name.isBlank()) {
            Result.failure(IllegalArgumentException("Name cannot be empty"))
        } else {
            Result.success(GreetingOutput("Hello, ${input.name} from UseCase!"))
        }
    }
}

/**
 * Example of a FlowUseCase for continuous data.
 */
data class CounterOutput(val value: Int) : UseCaseOutput

class GetCounterFlowUseCase : FlowUseCase<NoneInput, CounterOutput> {
    override fun invoke(input: NoneInput): Flow<CounterOutput> = flow {
        for (i in 1..5) {
            delay(1000)
            emit(CounterOutput(i))
        }
    }
}
