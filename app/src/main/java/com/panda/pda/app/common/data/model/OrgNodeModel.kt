package com.panda.pda.app.common.data.model

import java.io.Serializable

/**
 * created by AnJiwei 2021/9/26
 */
data class OrgNodeModel(
    var childNodeList: List<OrgNodeModel>,
    val nodeId: Int?,
    val nodeLevel: Int,
    val nodeName: String,
    val nodeType: Int,
    val orgNum: String
) : Serializable {
//    val isOrgNode: Boolean get() = nodeType != 2
    val isOrgNode: Boolean get() = nodeType == 1
}