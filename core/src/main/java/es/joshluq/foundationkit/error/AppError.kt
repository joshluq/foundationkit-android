package es.joshluq.foundationkit.error

/**
 * Standard sealed hierarchy representing infrastructure and platform errors across FoundationKit and dependent kits.
 *
 * Designed to replace untyped [Throwable] instances with structured, exhaustively handled error categories
 * without containing application-specific business logic.
 */
sealed interface AppError {

    /** Network connectivity or HTTP communication failure. */
    data class Network(
        val code: Int? = null,
        val message: String? = null,
        val cause: Throwable? = null
    ) : AppError

    /** Device is offline or has no active internet connection. */
    data class NoInternet(
        val message: String? = null
    ) : AppError

    /** An asynchronous operation timed out before completion. */
    data class Timeout(
        val timeoutMillis: Long? = null,
        val message: String? = null
    ) : AppError

    /** Persistence failure (Disk, SharedPreferences, Database). */
    data class Storage(
        val message: String? = null,
        val cause: Throwable? = null
    ) : AppError

    /** Data serialization or deserialization failure (JSON, binary). */
    data class Serialization(
        val message: String? = null,
        val cause: Throwable? = null
    ) : AppError

    /** Cryptographic failure (KeyStore, encryption, decryption). */
    data class Cryptographic(
        val message: String? = null,
        val cause: Throwable? = null
    ) : AppError

    /** Requested entity, key, or resource was not found. */
    data class NotFound(
        val resource: String? = null,
        val message: String? = null
    ) : AppError

    /** Required system permission was denied or not granted. */
    data class PermissionDenied(
        val permission: String? = null,
        val message: String? = null
    ) : AppError

    /** Authentication or session authorization failure (token expired, invalid credentials). */
    data class Authentication(
        val message: String? = null,
        val cause: Throwable? = null
    ) : AppError

    /** Input or format validation error. */
    data class Validation(
        val reason: String,
        val field: String? = null
    ) : AppError

    /** Unexpected, unhandled, or generic runtime error. */
    data class Unexpected(
        val cause: Throwable
    ) : AppError
}
