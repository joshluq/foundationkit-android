package es.joshluq.foundationkit.state

import es.joshluq.foundationkit.text.TextProvider

/**
 * Represents the state of a list of data in the UI.
 * This sealed interface allows for exhaustive handling of all possible states of a data collection.
 *
 * @param T The type of the elements in the list.
 */
sealed interface ListState<out T> {

    /**
     * The initial state, before any action has been taken.
     * Use this when the screen is first loaded and no data request has been made yet.
     */
    data object Idle : ListState<Nothing>

    /**
     * Represents that data is currently being fetched or processed.
     * Use this to show a loading indicator (e.g., ProgressBar, Skeleton).
     */
    data object Loading : ListState<Nothing>

    /**
     * Represents a successful fetch of data containing elements.
     * Use this to display the list of items.
     *
     * @property data The list of items retrieved.
     */
    data class Success<out T>(val data: List<T>) : ListState<T>

    /**
     * Represents a successful fetch of data, but the result is empty.
     * Use this to show an empty state view (e.g., "No items found").
     */
    data object Empty : ListState<Nothing>

    /**
     * Represents a failure in fetching or processing data.
     * Use this to display an error message or retry button.
     *
     * @property message The error message to be displayed.
     */
    data class Error(val message: TextProvider) : ListState<Nothing>
}

/**
 * Returns true if the state is [ListState.Loading].
 */
val ListState<*>.isLoading: Boolean
    get() = this is ListState.Loading

/**
 * Returns true if the state is [ListState.Error].
 */
val ListState<*>.isError: Boolean
    get() = this is ListState.Error

/**
 * Returns the data if the state is [ListState.Success], otherwise null.
 */
fun <T> ListState<T>.getOrNull(): List<T>? = (this as? ListState.Success)?.data

/**
 * Converts a [List] to a [ListState].
 * Returns [ListState.Empty] if the list is empty, otherwise [ListState.Success].
 */
fun <T> List<T>.toSuccessOrEmpty(): ListState<T> {
    return if (this.isEmpty()) ListState.Empty else ListState.Success(this)
}

/**
 * Converts a Kotlin [Result] containing a [List] to a [ListState].
 *
 * @param errorMapper A function to map the [Throwable] to a [TextProvider].
 *                    Defaults to wrapping the exception message in [TextProvider.Dynamic].
 */
fun <T> Result<List<T>>.toListState(
    errorMapper: (Throwable) -> TextProvider = {
        TextProvider.Dynamic(it.message ?: "Unknown error")
    }
): ListState<T> {
    return fold(
        onSuccess = { it.toSuccessOrEmpty() },
        onFailure = { ListState.Error(errorMapper(it)) }
    )
}
