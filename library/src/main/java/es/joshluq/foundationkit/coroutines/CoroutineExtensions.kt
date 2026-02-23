package es.joshluq.foundationkit.coroutines

import es.joshluq.foundationkit.log.Loggerkit
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Safely launches a coroutine and captures any [Exception] that occurs during its execution.
 *
 * This utility is part of the FoundationKit core to ensure that background tasks
 * don't crash the application and provide a unified way to handle errors.
 *
 * @param context Additional to [CoroutineScope.coroutineContext] context of the coroutine.
 * @param logger Optional [Loggerkit] to automatically log errors.
 * @param onError Optional callback invoked when an exception is caught.
 * @param block The suspendable block of code to execute.
 * @return The [Job] representing the launched coroutine.
 */
fun CoroutineScope.launchSafe(
    context: CoroutineContext = EmptyCoroutineContext,
    logger: Loggerkit? = null,
    onError: ((Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(context) {
        try {
            block()
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            // CancellationException should not be swallowed to allow coroutine cancellation to work properly.
            if (e is CancellationException) throw e

            withContext(NonCancellable) {
                logger?.e("launchSafe", "Unhandled exception in coroutine: ${e.message}", e)
            }
            onError?.invoke(e)
        }
    }
}

/**
 * Executes a suspendable block of code safely, returning a [Result].
 *
 * If an exception occurs, it is automatically logged using the provided [Loggerkit]
 * with the "SafeRunner" tag before returning a [Result.failure].
 *
 * @param logger The [Loggerkit] instance to use for error reporting.
 * @param block The suspendable block to execute.
 * @return A [Result] containing the success value or the caught exception.
 */
suspend fun <T> safeRun(
    logger: Loggerkit? = null,
    block: suspend () -> T
): Result<T> {
    return try {
        Result.success(block())
    } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
        // CancellationException should not be swallowed to allow coroutine cancellation to work properly.
        if (e is CancellationException) throw e

        withContext(NonCancellable) {
            logger?.e("SafeRunner", "Execution failed: ${e.message}", e)
        }

        Result.failure(e)
    }
}
