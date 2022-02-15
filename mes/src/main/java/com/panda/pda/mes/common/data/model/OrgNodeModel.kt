package com.panda.pda.mes.common.data.model

import java.io.Serializable

/**
 * created by AnJiwei 2021/9/26
 */
data class OrgNodeModel(
    val id: Int,
    var childNodeList: List<OrgNodeModel>,
    val nodeId: String,
    val nodeLevel: Int,
    val nodeName: String,
    val nodeType: Int,
    val orgNum: String
) : Serializable {
//    val isOrgNode: Boolean get() = nodeType != 2
    val isOrgNode: Boolean get() = nodeType == 1
    @Transient
    var department: OrgNodeModel? = null
}