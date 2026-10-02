package es.joshluq.foundationkit.platform

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AndroidPlatformInfoProvidersTest {

    @Test
    fun `AndroidAppInfoProvider extracts package metadata correctly`() {
        val context = mockk<Context>()
        val packageManager = mockk<PackageManager>()
        val appInfo = ApplicationInfo().apply {
            flags = ApplicationInfo.FLAG_DEBUGGABLE
            targetSdkVersion = 34
        }
        val packageInfo = PackageInfo().apply {
            versionName = "2.1.0"
            applicationInfo = appInfo
        }

        every { context.applicationContext } returns context
        every { context.packageName } returns "es.joshluq.test"
        every { context.packageManager } returns packageManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            every { packageManager.getPackageInfo("es.joshluq.test", any<PackageManager.PackageInfoFlags>()) } returns packageInfo
        } else {
            @Suppress("DEPRECATION")
            every { packageManager.getPackageInfo("es.joshluq.test", 0) } returns packageInfo
        }

        val provider = AndroidAppInfoProvider(context)
        val info = provider.getAppInfo()

        assertEquals("es.joshluq.test", info.packageName)
        assertEquals("2.1.0", info.versionName)
        assertTrue(info.isDebuggable)
        assertEquals(34, info.targetSdk)
    }

    @Test
    fun `AndroidDeviceInfoProvider returns valid device metadata`() {
        val context = mockk<Context>()
        val resources = mockk<Resources>()
        val configuration = Configuration().apply {
            screenLayout = Configuration.SCREENLAYOUT_SIZE_NORMAL
            smallestScreenWidthDp = 360
        }

        every { context.applicationContext } returns context
        every { context.resources } returns resources
        every { resources.configuration } returns configuration

        val provider = AndroidDeviceInfoProvider(context)
        val deviceInfo = provider.getDeviceInfo()

        assertNotNull(deviceInfo.manufacturer)
        assertNotNull(deviceInfo.model)
        assertNotNull(deviceInfo.device)
        assertNotNull(deviceInfo.osVersion)
    }

    @Test
    fun `Context extension functions instantiate providers correctly`() {
        val context = mockk<Context>()
        every { context.applicationContext } returns context

        val appInfoProvider = context.appInfoProvider()
        val deviceInfoProvider = context.deviceInfoProvider()

        assertNotNull(appInfoProvider)
        assertNotNull(deviceInfoProvider)
    }
}
