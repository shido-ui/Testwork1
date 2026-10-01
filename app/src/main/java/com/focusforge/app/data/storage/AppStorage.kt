package com.focusforge.app.data.storage

import android.content.Context
import java.io.File

class AppStorage(context: Context) {
    private val root = File(context.filesDir, "focusforge").apply { mkdirs() }
    val pdfs = File(root, "pdfs").apply { mkdirs() }
    val extracted = File(root, "extracted").apply { mkdirs() }
    val cache = File(root, "cache").apply { mkdirs() }

    fun availableBytes(): Long = root.usableSpace

    fun deleteCache() {
        cache.deleteRecursively()
        cache.mkdirs()
    }
}
