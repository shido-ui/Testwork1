package com.focusforge.app.core.permissions

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.provider.Settings

class PermissionChecker(private val context: Context) {
    fun current(): PermissionState {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val usage = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        ) == AppOpsManager.MODE_ALLOWED

        val overlay = Settings.canDrawOverlays(context)
        val notifications = Build.VERSION.SDK_INT < 33 ||
            context.checkSelfPermission("android.permission.POST_NOTIFICATIONS") ==
            android.content.pm.PackageManager.PERMISSION_GRANTED

        return PermissionState(notifications, usage, overlay)
    }
}
