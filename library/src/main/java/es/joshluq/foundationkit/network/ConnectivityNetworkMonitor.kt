package es.joshluq.foundationkit.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import es.joshluq.foundationkit.manager.toSafeContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Android implementation of [NetworkMonitor] using [ConnectivityManager].
 *
 * @param context The Android context (automatically converted to application context).
 */
class ConnectivityNetworkMonitor(
    context: Context
) : NetworkMonitor {

    private val safeContext = context.toSafeContext()
    private val connectivityManager =
        safeContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    override val status: Flow<NetworkStatus> = callbackFlow {
        if (connectivityManager == null) {
            trySend(NetworkStatus.Unavailable)
            close()
            return@callbackFlow
        }

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Available)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                trySend(NetworkStatus.Losing)
            }

            override fun onLost(network: Network) {
                trySend(NetworkStatus.Lost)
            }

            override fun onUnavailable() {
                trySend(NetworkStatus.Unavailable)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Emit current initial connectivity status
        val currentNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(currentNetwork)
        val hasInternet = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        trySend(if (hasInternet) NetworkStatus.Available else NetworkStatus.Unavailable)

        awaitClose {
            try {
                connectivityManager.unregisterNetworkCallback(callback)
            } catch (@Suppress("TooGenericExceptionCaught") _: Exception) {
                // Ignore unregister exceptions if already unregistered
            }
        }
    }.distinctUntilChanged().conflate()

    override val isOnline: Flow<Boolean> = status
        .map { it == NetworkStatus.Available }
        .distinctUntilChanged()
}

/**
 * Convenient extension function to obtain a [NetworkMonitor] instance from any [Context].
 *
 * @return A [ConnectivityNetworkMonitor] instance bound to the application context.
 */
fun Context.networkMonitor(): NetworkMonitor = ConnectivityNetworkMonitor(this)
