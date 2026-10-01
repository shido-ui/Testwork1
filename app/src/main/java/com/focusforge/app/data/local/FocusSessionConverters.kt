package com.focusforge.app.data.local

import org.json.JSONArray

object FocusSessionConverters {
    fun encodeAllowlist(packages: Set<String>): String = JSONArray(packages.sorted()).toString()

    fun decodeAllowlist(value: String): Set<String> = runCatching {
        val array = JSONArray(value)
        buildSet {
            for (index in 0 until array.length()) add(array.getString(index))
        }
    }.getOrDefault(emptySet())
}
