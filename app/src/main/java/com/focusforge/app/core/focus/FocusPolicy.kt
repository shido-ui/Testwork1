package com.focusforge.app.core.focus

interface FocusPolicy {
    fun canEnforce(): Boolean
    fun begin(allowlist: Set<String>)
    fun end()
}
