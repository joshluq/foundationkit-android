package es.joshluq.foundationkit.testing.platform

import es.joshluq.foundationkit.platform.AppInfo
import es.joshluq.foundationkit.platform.AppInfoProvider
import es.joshluq.foundationkit.platform.DeviceInfo
import es.joshluq.foundationkit.platform.DeviceInfoProvider

/**
 * Fake implementation of [AppInfoProvider] for testing without Android Context dependencies.
 */
class FakeAppInfoProvider(
    var currentAppInfo: AppInfo =
        AppInfo(
            packageName = "es.joshluq.fake",
            versionName = "1.0.0-test",
            versionCode = 1L,
            isDebuggable = true,
            minSdk = 26,
            targetSdk = 34,
        ),
) : AppInfoProvider {
    override fun getAppInfo(): AppInfo = currentAppInfo
}

/**
 * Fake implementation of [DeviceInfoProvider] for testing without Android Context dependencies.
 */
class FakeDeviceInfoProvider(
    var currentDeviceInfo: DeviceInfo =
        DeviceInfo(
            manufacturer = "TestManufacturer",
            model = "TestModel",
            device = "TestDevice",
            osVersion = "14.0",
            sdkInt = 34,
            isTablet = false,
            isEmulator = false,
        ),
) : DeviceInfoProvider {
    override fun getDeviceInfo(): DeviceInfo = currentDeviceInfo
}
