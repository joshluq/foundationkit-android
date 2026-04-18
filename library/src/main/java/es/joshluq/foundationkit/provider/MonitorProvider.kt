package es.joshluq.foundationkit.provider

/**
 * Interface to be implemented by the consumer application to provide
 * integration with third-party monitoring services (e.g., Firebase, Sentry).
 */
interface MonitorProvider<E, M> : Provider {

    /**
     * Unique identifier for the provider.
     */
    val key: String

    /**
     * Sends a custom event to the monitoring service.
     *
     * @param event The event to be tracked.
     */
    suspend fun trackEvent(event: E)

    /**
     * Sends a performance metric to the monitoring service.
     *
     * @param metric The metric to be recorded.
     */
    suspend fun trackMetric(metric: M)

    /**
     * Sets a global attribute for this provider.
     *
     * @param key The attribute key.
     * @param value The attribute value.
     */
    fun setAttribute(key: String, value: String) {}

    /**
     * Sets multiple global attributes for this provider.
     *
     * @param attributes A map of key-value pairs to set as attributes.
     */
    fun setAttributes(attributes: Map<String, String>) {
        attributes.forEach { (key, value) -> setAttribute(key, value) }
    }

    /**
     * Removes a global attribute from this provider.
     *
     * @param key The attribute key to remove.
     */
    fun removeAttribute(key: String) {}

    /**
     * Removes multiple global attributes from this provider.
     *
     * @param keys The list of attribute keys to remove.
     */
    fun removeAttributes(keys: List<String>) {
        keys.forEach { removeAttribute(it) }
    }

    /**
     * Starts a native trace on the provider.
     * @param traceKey Unique name of the trace.
     * @param properties Initial properties.
     */
    suspend fun startTrace(traceKey: String, properties: Map<String, Any>? = null) {}

    /**
     * Stops a native trace on the provider.
     * @param traceKey Unique name of the trace.
     * @param properties Final properties to attach.
     */
    suspend fun stopTrace(traceKey: String, properties: Map<String, Any>? = null) {}

    /**
     * Cancels a native trace on the provider.
     * @param traceKey Unique name of the trace.
     */
    suspend fun cancelTrace(traceKey: String) {}
}
