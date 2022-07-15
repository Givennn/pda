package com.panda.pda.mes.common.data.model

/**
 * created by AnJiwei 2021/9/6
 */
data class DataParameterModel(
    val ALARM_STATUS: List<Parameter>,
    val ENABLE_STATUS: List<Parameter>,
    val SOURCE_TYPE: List<Parameter>,
    val TASK_STATUS: List<Parameter>,
    val CONCLUSION_DECIDE_OPTION: List<Parameter>,
    val QUALITY_PROBLEM_STATUS: List<Parameter>,
    val VALUE_TYPE: List<Parameter>,
    val QUALITY_TYPE: List<Parameter>,
    val SUB_QUALITY_TASK_STATUS: List<Parameter>,
    val RECORD_METHOD: List<Parameter>,
    val QUALITY_TASK_STATUS: List<Parameter>,
    val QUALITY_METHOD: List<Parameter>,
    //EMS新增配置  start by yinzl
    //设备功能状态
    val FUNCTION_STATUS_PDA: List<Parameter>,
    //设备详情中位置状态
    val LOCATION_STATUS: List<Parameter>,
    //设备类型   设备/模具
    val FACILITY_TYPE: List<Parameter>,
    //功能类型
    val FUNCTION_TYPE: List<Parameter>,
    //工单状态
    val WORK_ORDER_STATUS: List<Parameter>,
    //工单状态对应的文字
    val WORK_ORDER_OPERATE: List<Parameter>,
    val BPS_PRODUCT_MODE: List<Parameter>,
)

data class Parameter(
    val id: Int,
    val paramDesc: String,
    val paramName: String,
    val paramValue: Int,
    val parentId: Int,
)