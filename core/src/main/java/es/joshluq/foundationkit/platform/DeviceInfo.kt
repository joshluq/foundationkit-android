package es.joshluq.foundationkit.platform

/**
 * Immutable metadata representing the underlying hardware and OS environment.
 *
 * @property manufacturer The manufacturer of the product/hardware (e.g., "Google", "Samsung").
 * @property model The end-user-visible name for the end product (e.g., "Pixel 8").
 * @property device The industrial device design name.
 * @property osVersion The user-visible OS version string (e.g., "14").
 * @property sdkInt The SDK API level integer of the OS (e.g., 34).
 * @property isTablet Whether the device has a tablet form factor.
 * @property isEmulator Whether the device is running inside an emulator.
 */
data class DeviceInfo(
    val manufacturer: String,
    val model: String,
    val device: String,
    val osVersion: String,
    val sdkInt: Int,
    val isTablet: Boolean,
    val isEmulator: Boolean,
)
