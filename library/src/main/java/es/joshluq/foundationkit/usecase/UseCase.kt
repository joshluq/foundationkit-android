package es.joshluq.foundationkit.usecase

import kotlinx.coroutines.flow.Flow

/**
 * Base interface for all use case inputs.
 */
interface UseCaseInput

/**
 * Base interface for all use case outputs.
 */
interface UseCaseOutput

/**
 * Represents a generic Use Case in Clean Architecture.
 *
 * @param I Input type that must implement [UseCaseInput].
 * @param O Output type that must implement [UseCaseOutput].
 */
interface UseCase<in I : UseCaseInput, out O : UseCaseOutput> {
    /**
     * Executes the business logic of the use case.
     *
     * @param input The parameters required for the operation.
     * @return A [Result] containing the operation output or an exception.
     */
    suspend operator fun invoke(input: I): Result<O>
}

/**
 * Represents a Flow-based Use Case for reactive streams.
 *
 * @param I Input type that must implement [UseCaseInput].
 * @param O Output type that must implement [UseCaseOutput].
 */
interface FlowUseCase<in I : UseCaseInput, out O : UseCaseOutput> {
    /**
     * Executes the business logic and returns a cold [Flow].
     *
     * @param input The parameters required for the operation.
     * @return A [Flow] emitting objects.
     */
    operator fun invoke(input: I): Flow<O>
}

/**
 * Represents a standard empty input for Use Cases that do not require parameters.
 */
object NoneInput : UseCaseInput

/**
 * Represents a standard empty output for Use Cases that do not return a specific value.
 */
object NoneOutput : UseCaseOutput
