package es.joshluq.foundationkit.error

import es.joshluq.foundationkit.state.ListState
import es.joshluq.foundationkit.text.TextProvider

/**
 * Converts an [AppError] into a user-facing [TextProvider].
 *
 * @return A [TextProvider.Dynamic] containing the diagnostic message for the error.
 */
fun AppError.toTextProvider(): TextProvider {
    val message =
        when (this) {
            is AppError.Network -> message ?: "Network error (code: ${code ?: "unknown"})"
            is AppError.NoInternet -> message ?: "No internet connection"
            is AppError.Timeout -> message ?: "Operation timed out"
            is AppError.Storage -> message ?: "Storage operation failed"
            is AppError.Serialization -> message ?: "Data parsing failed"
            is AppError.Cryptographic -> message ?: "Cryptographic operation failed"
            is AppError.NotFound -> message ?: "Resource not found: ${resource ?: "unknown"}"
            is AppError.PermissionDenied -> message ?: "Permission denied: ${permission ?: "required"}"
            is AppError.Authentication -> message ?: "Authentication failed"
            is AppError.Validation -> "Validation error in ${field ?: "input"}: $reason"
            is AppError.Unexpected -> cause.message ?: "An unexpected error occurred"
        }
    return TextProvider.Dynamic(message)
}

/**
 * Converts an [AppError] into a [ListState.Error] state for UI consumption.
 *
 * @return A [ListState.Error] wrapping the mapped [TextProvider].
 */
fun AppError.toListStateError(): ListState.Error = ListState.Error(toTextProvider())
