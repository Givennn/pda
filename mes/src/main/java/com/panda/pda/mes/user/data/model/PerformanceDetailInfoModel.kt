package com.panda.pda.mes.user.data.model

import com.panda.pda.mes.common.ModelProperty
import java.math.BigDecimal

/**
 * created by AnJiwei 2022/9/2
 */
data class PerformanceDetailInfoModel(
    val detailInfo: DetailInfo,
    val itemList: List<Item>
)

data class DetailInfo(
    val createId: Int,
    val createName: String,
    val createTime: String,
    val deployDate: String,
    val dutyCode: String,
    @ModelProperty(4, "职务")
    val dutyName: String,
    val hourlyWage: Double,
    val id: Int,
    val orgId: Int,
    @ModelProperty(3, "组织部门")
    val orgName: String,
    @ModelProperty(7,"考评系数")
    val performance: Int,
    @ModelProperty(8, "实得绩效（元）")
    val realAmount: String,
    val remark: String,
    @ModelProperty(5, "统计时间")
    val statisticsTime: String,
    val status: Int,
    val statusDesc: String,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val userId: Int,
    @ModelProperty(1, "用户ID")
    val workCode: String,
    @ModelProperty(2, "用户姓名")
    val userName: String,
)

data class Item(
    val createId: Int,
    val createName: String,
    val createTime: String,
    val id: Int,
    val performanceAmount: String,
    val performanceId: Int,
    val performanceItemCode: String,
    val performanceItemName: String,
    val performanceValue: String,
    val performanceWeight: String,
    val remark: String,
    val updateId: Int,
    val updateName: String,
    val updateTime: String
)