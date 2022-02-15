package com.panda.pda.mes.base.retrofit

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor
import java.nio.charset.Charset

/**
 * created by AnJiwei 2021/9/8
 */
class OkHttpLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        okHttpLog(message)
    }

    private fun okHttpLog(message: String, level: Int = Log.DEBUG, t: Throwable? = null) {
        val maxLogLength = 4000
        val tag = "OkHttp"
        val encoder = Charset.forName("ISO-8859-1").newEncoder()

        var logMessage = message
        if (t != null) logMessage = logMessage + '\n'.toString() + Log.getStackTraceString(t)

        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = logMessage.length
        var isBinaryLogDisplayed = false
        var isBinaryContentType = false
        while (i < length) {
            var newline = logMessage.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = minOf(newline, i + maxLogLength)
                val msg = logMessage.substring(i, end).trim()

                if (msg.contains("Content-Type") &&
                    msg.contains("application/octet-stream")) { // use another Content-Type if need
                    isBinaryContentType = true
                }
                val isBinaryData = !encoder.canEncode(msg)

                // multipart boundary
                if (isBinaryLogDisplayed && msg.startsWith("--")) {
                    isBinaryContentType = false
                    isBinaryLogDisplayed = false
                }

                // don't print binary data
                if (isBinaryContentType && isBinaryData && !isBinaryLogDisplayed) {
                    Log.println(level, tag, "<BINARY DATA>")
                    isBinaryLogDisplayed = true
                }

                if (!isBinaryLogDisplayed) {
                    Log.println(level, tag, msg)
                }

                i = end
            } while (i < newline)
            i++
        }
    }
}