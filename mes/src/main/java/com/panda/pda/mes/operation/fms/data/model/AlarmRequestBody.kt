package com.panda.pda.mes.operation.fms.data.model

/**
 * created by AnJiwei 2021/9/1
 */
data class AlarmDetailRequest(
    var alarmDetail: String? = null
) {
    var exceptionTypes: Int? = null
    var breakdownFlag: Int = 2
    var emergencyFlag: Int = 2
    var equipmentCode: String? = null
    var equipmentDesc: String? = null
}