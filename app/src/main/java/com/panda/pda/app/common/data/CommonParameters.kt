package com.panda.pda.app.common.data

import com.panda.pda.app.common.data.model.DataParameterModel
import com.panda.pda.app.common.data.model.Parameter

/**
 * created by AnJiwei 2021/9/16
 */
object CommonParameters {
    private var dictionary = mutableMapOf<DataParamType, List<Parameter>>()

    fun getDesc(paramType: DataParamType, value: Int): String {
        return dictionary[paramType]?.firstOrNull { it.paramValue == value }?.paramDesc
            ?: "undefined"
    }

    fun pushParameters(data: DataParameterModel) {
        dictionary[DataParamType.ENABLE_STATUS] = data.ENABLE_STATUS
        dictionary[DataParamType.TASK_STATUS] = data.TASK_STATUS
        dictionary[DataParamType.ALARM_STATUS] = data.ALARM_STATUS
    }
}

enum class DataParamType {
    ALARM_STATUS,
    ENABLE_STATUS,
    SOURCE_TYPE,
    TASK_STATUS
}