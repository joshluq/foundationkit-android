package es.joshluq.foundationkit.platform

/**
 * Immutable metadata representing the current application environment.
 *
 * @property packageName The application's unique package identifier.
 * @property versionName The human-readable version string (e.g., "1.0.0").
 * @property versionCode The internal version number used for upgrade checks.
 * @property isDebuggable Whether the application was compiled in debug mode.
 * @property minSdk The minimum SDK version supported by the application, if available.
 * @property targetSdk The target SDK version configured for the application, if available.
 */
data class AppInfo(
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val isDebuggable: Boolean,
    val minSdk: Int? = null,
    val targetSdk: Int? = null
)
