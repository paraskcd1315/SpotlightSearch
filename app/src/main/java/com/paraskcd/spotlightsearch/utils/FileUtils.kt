package com.paraskcd.spotlightsearch.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File

object FileUtils {
    fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    fun openFile(context: Context, uri: Uri, file: File) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, getMimeType(file))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, "Abrir con...")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getMimeType(file: File): String {
        val extension = file.extension.lowercase()
        return MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(extension) ?: "*/*"
    }
}