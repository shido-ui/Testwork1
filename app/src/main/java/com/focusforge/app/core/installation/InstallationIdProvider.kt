package com.focusforge.app.core.installation

import android.content.Context
import java.util.UUID

class InstallationIdProvider(context: Context) {
    private val prefs = context.getSharedPreferences("installation", Context.MODE_PRIVATE)
    fun getOrCreate(): String =
        prefs.getString(KEY, null) ?: UUID.randomUUID().toString().also {
            prefs.edit().putString(KEY, it).apply()
        }
    private companion object { const val KEY = "installation_id" }
}
