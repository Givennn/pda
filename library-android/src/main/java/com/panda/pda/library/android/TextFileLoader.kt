package com.panda.pda.library.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.FileReader

/**
 * created by AnJiwei 2021/1/14
 */
class TextFileLoader {
    companion object {
        fun readFile(context: Context, documentUri: Uri): String {

            val resolver = context.contentResolver
            resolver.takePersistableUriPermission(documentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val fileDescriptor = resolver.openFileDescriptor(documentUri, "r") ?: throw NoSuchFieldException()

            val reader = FileReader(fileDescriptor.fileDescriptor)
            val text = reader.readText()
            reader.close()
            return text
        }

        fun readFile(path: String): String {
            val reader = FileReader(path)
            val text = reader.readText()
            reader.close()
            return text
        }
    }
}