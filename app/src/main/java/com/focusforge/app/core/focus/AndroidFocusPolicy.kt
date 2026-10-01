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
        val manager = dpm ?: error("DevicePolicyManager unavailable")
        require(canEnforce()) { "Device Owner Lock Task is not configured" }
        manager.setLockTaskPackages(admin, (allowlist + context.packageName).toTypedArray())
        (context as? Activity)?.startLockTask()
            ?: error("Lock Task requires an Activity context")
    }

    override fun end() {
        (context as? Activity)?.stopLockTask()
    }
}
