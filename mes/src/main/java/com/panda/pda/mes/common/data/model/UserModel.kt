package com.panda.pda.mes.common.data.model

/**
 * created by AnJiwei 2022/7/15
 */

data class UserModel(
    val createId: Int,
    val createName: String,
    val createTime: String,
    val email: String,
    val id: Int,
    val mobile: String,
    val orgId: Int,
    val orgName: String,
    val personCode: String,
    val phoneNumber: String,
    val pinyinName: String,
    val realName: String,
    val remark: String,
    val roleId: List<Int>,
    val roleIds: List<Int>,
    val roleList: List<Role>,
    val sex: Int,
    val status: Int,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val userName: String,
    val workCode: String
)

data class Role(
    val createId: Int,
    val createName: String,
    val createTime: String,
    val id: Int,
    val remark: String,
    val roleDesc: String,
    val roleName: String,
    val updateId: Int,
    val updateName: String,
    val updateTime: String
)