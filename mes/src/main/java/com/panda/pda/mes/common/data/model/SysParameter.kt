package com.panda.pda.mes.common.data.model

/**
 * created by AnJiwei 2022/12/5
 */

data class SysParameter(
    val id: Double,
    val sysModules: String,
    val sysParam: String,
    val sysValue: String,
    val updateTime: String,
)

enum class SysModule {
    FMS,
    QMS,
    SOG,
    BPS
}

@Suppress("EnumEntryName")
enum class QMSModuleProperty {
    taskAutoReceive, //：任务接收方式 0手动 1自动
    taskAutoReceiveIds, //：_任务自动接收人 _ 1,2,4
    taskAutoCommit, //：任务自动提交0手动 1自动
    taskAutoCommitAuditId, //：任务自动提交审核人
    taskNeedAudit, //：审核必要性 0 不必要 1必要
    taskAutoDistribute, // : 质检任务派发方式 0手动 1自动
    taskAutoDistributeId, // : 质检任务自动派发人
    taskAutoSign, //: 是否自动签收 0手动 1自动
    taskAutoComplete, //：任务完工方式 0手动 1自动
    taskRunType // : 质检任务执行方式 1条码 2编码
}

enum class TaskRunType(val code: Int) {
    SerialCode(1),
    Encode(2);

    companion object {
        fun getTaskRunType(parameter: SysParameter): TaskRunType {
            if (parameter.sysParam != QMSModuleProperty.taskRunType.name) {
                throw EnumConstantNotPresentException(TaskRunType::class.java, parameter.sysValue)
            }
            return values().first { it.code == parameter.sysValue.toInt() }
        }
    }

}

//sysModules :  FMS
//字段含义
//taskAutoReceive：任务接收方式 0手动 1自动
//producePreNeed：生产准备必要性 0非必要 1必要
//taskAutoComplete：任务完工方式 0手动 1自动
//productBarEdit：产品条码可编辑性 0否 1是
//
//sysModules : QMS
//字段含义
//taskAutoReceive：任务接收方式 0手动 1自动
//taskAutoReceiveIds：_任务自动接收人 _ 1,2,4
//taskAutoCommit：任务自动提交0手动 1自动
//taskAutoCommitAuditId：任务自动提交审核人
//taskNeedAudit：审核必要性 0 不必要 1必要
//taskAutoDistribute : 质检任务派发方式 0手动 1自动
//taskAutoDistributeId : 质检任务自动派发人
//taskAutoSign: 是否自动签收 0手动 1自动
//taskAutoComplete：任务完工方式 0手动 1自动
//taskRunType: 质检任务执行方式 1条码 2编码
//
//
//
//sysModules : SOG
//字段含义
//taskSource：生产任务来源  1-无任务 2-MES任务 3-外系统任务
//
//sysModules : BPS
//字段含义
//equipmentType：生产设备对接系统，1-是 2-否
//reportType：报工方式 1-工单报工 2-派工单报工
//receiveType：工单接收方式 1-自动 2-手动
//completeType：工单完工方式 1-自动 2-手动
//scheduleType：工单排产周期 1-按天 2-按周 3-按月
//produceType：生产方式 1-自定义流转 2-批量流转
//produceTypeNum:自定义流转的值
//pdaCodeLogin：pda是否有扫码登录  1-是 2-否
//planSource:主计划来源  1-MES 2-SAP