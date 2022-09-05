package com.panda.pda.mes.user.data.model

/**
 * created by AnJiwei 2022/9/2
 */
data class SkillItemModel(
    val createId: Int,
    val createName: String,
    val createTime: String,
    val id: Int,
    val performanceId: Int,
    val performancePoints: Double,
    val remark: String,
    val skillDetailId: Int,
    val skillId: Int,
    val skillLevel: Int,
    val skillName: String,
    val updateId: Int,
    val updateName: String,
    val updateTime: String
)