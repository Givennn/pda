package com.panda.pda.app.common.data.model

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
    val SUB_QUALITY_TASK_STATUS: List<Parameter>
)

data class Parameter(
    val id: Int,
    val paramDesc: String,
    val paramName: String,
    val paramValue: Int,
    val parentId: Int,
)