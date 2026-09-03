package es.joshluq.foundationkit.provider

/**
 * Interface for analytics providers. Implement this interface to route analytics data to specific
 * services (e.g., Firebase, Mixpanel).
 */
interface AnalyticsProvider<T> : Provider {

    /** Unique identifier for the provider. */
    val key: String

    /**
     * Tracks an analytics event.
     *
     * @param event The event to be tracked.
     */
    suspend fun track(event: T)

    /**
     * Adds a global property to the provider. These properties are usually sent with every event.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     */
    fun addGlobalProperty(key: String, value: Any)

    /**
     * Removes a global property from the provider.
     *
     * @param key The key of the property to be removed.
     */
    fun removeGlobalProperty(key: String)
}
