package es.joshluq.foundationkit.platform

import android.content.Context

/**
 * Creates an [AppInfoProvider] instance bound to the application context.
 */
fun Context.appInfoProvider(): AppInfoProvider = AndroidAppInfoProvider(this)

/**
 * Creates a [DeviceInfoProvider] instance bound to the application context.
 */
fun Context.deviceInfoProvider(): DeviceInfoProvider = AndroidDeviceInfoProvider(this)
