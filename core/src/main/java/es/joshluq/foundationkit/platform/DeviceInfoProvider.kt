package es.joshluq.foundationkit.platform

import es.joshluq.foundationkit.provider.Provider

/**
 * Provider contract for querying hardware and operating system metadata.
 */
interface DeviceInfoProvider : Provider {
    /**
     * Retrieves the current [DeviceInfo].
     */
    fun getDeviceInfo(): DeviceInfo
}
