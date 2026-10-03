package es.joshluq.foundationkit.network

/**
 * Represents the current status of the network connection.
 */
enum class NetworkStatus {
    /** Network is connected and has internet capability. */
    Available,

    /** Network is not available or disconnected. */
    Unavailable,

    /** Network connection is currently weakening or in the process of being lost. */
    Losing,

    /** Network connection was previously available and has now been lost. */
    Lost,
}
