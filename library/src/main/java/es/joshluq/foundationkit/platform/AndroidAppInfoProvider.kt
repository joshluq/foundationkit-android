package es.joshluq.foundationkit.platform

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import es.joshluq.foundationkit.manager.toSafeContext

/**
 * Android implementation of [AppInfoProvider] reading metadata from [PackageManager].
 *
 * @param context The Android context used to access package metadata.
 */
class AndroidAppInfoProvider(context: Context) : AppInfoProvider {

    private val safeContext: Context = context.toSafeContext()

    override fun getAppInfo(): AppInfo {
        val packageName = safeContext.packageName
        val packageManager = safeContext.packageManager

        val packageInfo: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }

        val versionName = packageInfo.versionName ?: "0.0.0"
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }

        val appInfo = packageInfo.applicationInfo
        val isDebuggable = if (appInfo != null) {
            (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        } else {
            false
        }

        val minSdk = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && appInfo != null) {
            appInfo.minSdkVersion
        } else {
            null
        }

        val targetSdk = appInfo?.targetSdkVersion

        return AppInfo(
            packageName = packageName,
            versionName = versionName,
            versionCode = versionCode,
            isDebuggable = isDebuggable,
            minSdk = minSdk,
            targetSdk = targetSdk
        )
    }
}
