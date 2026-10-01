package com.focusforge.app.core.focus

import android.os.SystemClock

class SystemFocusClock : FocusClock {
    override fun epochMs(): Long = System.currentTimeMillis()
    override fun elapsedMs(): Long = SystemClock.elapsedRealtime()
}
