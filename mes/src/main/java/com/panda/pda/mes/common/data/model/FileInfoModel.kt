package com.panda.pda.mes.common.data.model

import android.net.Uri
import java.io.Serializable

/**
 * created by AnJiwei 2021/9/1
 */
data class FileInfoModel(
    val fileName: String,
    val fileUrl: String,
    val fileSize: String
) : Serializable {
    @Transient
    var fileLocalUri: Uri? = null
}
