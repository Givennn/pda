package com.panda.pda.mes.message.model

/**
 * Author:yinzhilin
 * Date: 2023/1/10
 * Desc:消息对象
 */
data class MessageItemModel(
    //
    val id: Int,
    //消息
    val message: String,
    //消息类型名称
    val messageTypeName: String,
    //消息模板名称
    val messageTemplateName: String,
    //模板创建人
    val templateCreateName: String,
    //创建时间
    val createTime: String,
    //来源
    val appName: String,
    //优先级 0=低 1=中 2=高
    val priority: Int,
)