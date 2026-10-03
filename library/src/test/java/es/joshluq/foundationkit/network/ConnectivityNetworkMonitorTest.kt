package es.joshluq.foundationkit.network

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ConnectivityNetworkMonitorTest {
    @Test
    fun `when connectivityManager is null, status is Unavailable and isOnline is false`() =
        runTest {
            val context = mockk<Context>()
            every { context.applicationContext } returns context
            every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns null

            val monitor = ConnectivityNetworkMonitor(context)

            assertEquals(NetworkStatus.Unavailable, monitor.status.first())
            assertFalse(monitor.isOnline.first())
        }
}
