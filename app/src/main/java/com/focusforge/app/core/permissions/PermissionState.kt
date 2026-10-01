package com.focusforge.app.core.permissions

data class PermissionState(
    val notificationsGranted: Boolean,
    val usageAccessGranted: Boolean,
    val overlayGranted: Boolean
) {
    val analyticsReady: Boolean get() = usageAccessGranted
}
