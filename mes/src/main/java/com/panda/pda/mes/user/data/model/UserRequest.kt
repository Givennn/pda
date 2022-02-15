package com.panda.pda.mes.user.data.model

/**
 * created by AnJiwei 2021/9/8
 */
data class PwdCheckRequest(
    val oldPassword: String
)

data class PwdModifyRequest(
    val oldPassword: String,
    val newPassword: String
)