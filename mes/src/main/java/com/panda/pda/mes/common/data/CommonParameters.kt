package com.panda.pda.mes.common.data

import com.panda.pda.mes.common.data.model.DataParameterModel
import com.panda.pda.mes.common.data.model.Parameter

/**
 * created by AnJiwei 2021/9/16
 */
object CommonParameters {
    private var dictionary = mutableMapOf<DataParamType, List<Parameter>>()

    fun getDesc(paramType: DataParamType, value: Int): String {
        return dictionary[paramType]?.firstOrNull { it.paramValue == value }?.paramDesc
            ?: "undefined"
    }

    fun getParameters(paramType: DataParamType): List<Parameter> {
        return dictionary[paramType] ?: listOf()
    }

    fun pushParameters(data: DataParameterModel) {
        dictionary[DataParamType.ENABLE_STATUS] = data.ENABLE_STATUS
        dictionary[DataParamType.TASK_STATUS] = data.TASK_STATUS
        dictionary[DataParamType.ALARM_STATUS] = data.ALARM_STATUS
        dictionary[DataParamType.SOURCE_TYPE] = data.SOURCE_TYPE
        dictionary[DataParamType.CONCLUSION_DECIDE_OPTION] = data.CONCLUSION_DECIDE_OPTION
        dictionary[DataParamType.QUALITY_PROBLEM_STATUS] = data.QUALITY_PROBLEM_STATUS
        dictionary[DataParamType.VALUE_TYPE] = data.VALUE_TYPE
        dictionary[DataParamType.QUALITY_TYPE] = data.QUALITY_TYPE
        dictionary[DataParamType.SUB_QUALITY_TASK_STATUS] = data.SUB_QUALITY_TASK_STATUS
        dictionary[DataParamType.RECORD_METHOD] = data.RECORD_METHOD
        dictionary[DataParamType.QUALITY_TASK_STATUS] = data.QUALITY_TASK_STATUS
        dictionary[DataParamType.QUALITY_METHOD] = data.QUALITY_METHOD
    }
}

enum class DataParamType {
    ALARM_STATUS,
    ENABLE_STATUS,
    SOURCE_TYPE,
    TASK_STATUS,
    CONCLUSION_DECIDE_OPTION,
    QUALITY_PROBLEM_STATUS,
    VALUE_TYPE,
    QUALITY_TYPE,
    SUB_QUALITY_TASK_STATUS,
    RECORD_METHOD,
    QUALITY_TASK_STATUS,
    QUALITY_METHOD
}