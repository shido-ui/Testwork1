package com.focusforge.app.core.storage

import android.content.Context
import android.net.Uri
import com.focusforge.app.data.storage.AppStorage
import java.io.File
import java.io.IOException

class DocumentImporter(
    private val context: Context,
    private val storage: AppStorage
) {
    @Throws(IOException::class)
    fun importPdf(uri: Uri): File {
        val name = "pdf-${System.currentTimeMillis()}.pdf"
        val destination = File(storage.pdfs, name)
        val resolver = context.contentResolver
        resolver.openInputStream(uri)?.use { input ->
            destination.outputStream().use { output -> input.copyTo(output) }
        } ?: throw IOException("Unable to open selected document")
        if (destination.length() == 0L) {
            destination.delete()
            throw IOException("Selected document is empty")
        }
        return destination
    }
}
