package es.joshluq.foundationkit.network

import kotlinx.coroutines.flow.Flow

/**
 * Reactive monitor that observes device network connectivity changes.
 *
 * Implementations should provide continuous updates regarding network reachability
 * and specific status transitions (available, losing, lost, unavailable).
 */
interface NetworkMonitor {
    /**
     * A cold [Flow] emitting `true` whenever an active network with internet capability is available,
     * and `false` otherwise.
     */
    val isOnline: Flow<Boolean>

    /**
     * A cold [Flow] emitting granular [NetworkStatus] state changes.
     */
    val status: Flow<NetworkStatus>
}
