package com.panda.pda.app.common.data.model

/**
 * created by AnJiwei 2021/9/26
 */
data class OrgNodeModel(
    val childNodeList: List<OrgNodeModel>,
    val nodeId: Int,
    val nodeLevel: Int,
    val nodeName: String,
    val nodeType: Int,
    val orgNum: String
)