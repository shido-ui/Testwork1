package com.focusforge.app.core.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object PermissionIntents {
    fun usageAccess(context: Context): Intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    fun overlay(context: Context): Intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:${context.packageName}")
    )
}
