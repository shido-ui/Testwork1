package com.focusforge.app.core.focus

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context

class AndroidFocusPolicy(
    private val context: Context,
    private val admin: ComponentName
) : FocusPolicy {
    private val dpm = context.getSystemService(DevicePolicyManager::class.java)

    override fun canEnforce(): Boolean =
        dpm?.isDeviceOwnerApp(context.packageName) == true &&
            dpm.isLockTaskPermitted(context.packageName)

    override fun begin(allowlist: Set<String>) {
        require(canEnforce()) { "Device Owner Lock Task is not configured" }
        dpm.setLockTaskPackages(admin, (allowlist + context.packageName).toTypedArray())
        (context as? Activity)?.startLockTask()
    }

    override fun end() {
        (context as? Activity)?.stopLockTask()
    }
}
