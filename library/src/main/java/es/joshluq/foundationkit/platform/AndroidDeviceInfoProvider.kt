package es.joshluq.foundationkit.platform

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import es.joshluq.foundationkit.manager.toSafeContext

/**
 * Android implementation of [DeviceInfoProvider] extracting hardware and OS properties.
 *
 * @param context The Android context used to query display metrics and resources.
 */
class AndroidDeviceInfoProvider(
    context: Context,
) : DeviceInfoProvider {
    private val safeContext: Context = context.toSafeContext()

    override fun getDeviceInfo(): DeviceInfo =
        DeviceInfo(
            manufacturer = Build.MANUFACTURER.orEmpty(),
            model = Build.MODEL.orEmpty(),
            device = Build.DEVICE.orEmpty(),
            osVersion = Build.VERSION.RELEASE.orEmpty(),
            sdkInt = Build.VERSION.SDK_INT,
            isTablet = checkIsTablet(),
            isEmulator = checkIsEmulator(),
        )

    private fun checkIsTablet(): Boolean {
        val screenLayout = safeContext.resources.configuration.screenLayout
        val isLargeOrXLarge = (screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
        val smallestWidthDp = safeContext.resources.configuration.smallestScreenWidthDp
        return isLargeOrXLarge || smallestWidthDp >= 600
    }

    private fun checkIsEmulator(): Boolean {
        val fingerprint = Build.FINGERPRINT.orEmpty().lowercase()
        val model = Build.MODEL.orEmpty().lowercase()
        val manufacturer = Build.MANUFACTURER.orEmpty().lowercase()
        val brand = Build.BRAND.orEmpty().lowercase()
        val device = Build.DEVICE.orEmpty().lowercase()
        val product = Build.PRODUCT.orEmpty().lowercase()
        val hardware = Build.HARDWARE.orEmpty().lowercase()

        return fingerprint.startsWith("generic") ||
            fingerprint.startsWith("unknown") ||
            model.contains("google_sdk") ||
            model.contains("emulator") ||
            model.contains("android sdk built for x86") ||
            manufacturer.contains("genymotion") ||
            (brand.startsWith("generic") && device.startsWith("generic")) ||
            product.contains("sdk") ||
            product.contains("google_sdk") ||
            product.contains("sdk_gphone") ||
            product.contains("vbox86p") ||
            product.contains("emulator") ||
            product.contains("simulator") ||
            hardware.contains("goldfish") ||
            hardware.contains("ranchu")
    }
}
