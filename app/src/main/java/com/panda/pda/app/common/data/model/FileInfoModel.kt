package com.panda.pda.app.common.data.model

import android.net.Uri

/**
 * created by AnJiwei 2021/9/1
 */
data class FileInfoModel(
    val fileName: String,
    val fileUrl: String,
    val fileSize: String
) {
    @Transient
    var fileLocalUri: Uri? = null
}
