package es.joshluq.foundationkit.platform

import es.joshluq.foundationkit.provider.Provider

/**
 * Provider contract for querying application-level metadata.
 */
interface AppInfoProvider : Provider {
    /**
     * Retrieves the current [AppInfo].
     */
    fun getAppInfo(): AppInfo
}
