package com.panda.pda.mes.operation.qms.data.model

import com.panda.pda.mes.common.ModelProperty
import com.panda.pda.mes.common.data.model.FileInfoModel

/**
 * created by AnJiwei 2021/9/26
 */
data class QualityProblemRecordDetailModel(
    @ModelProperty(1, "质检问题记录")
    var problemCode: String?,
    @ModelProperty(2, "产品条码")
    var productBarCode: String?,
    @ModelProperty(3, "产品编码")
    var productCode: String?,
    @ModelProperty(4, "产品描述")
    var productName: String?,
    @ModelProperty(5, "发生地")
    var occurrencePlace: String?,
    @ModelProperty(6, "质检任务编号")
    var qualityCode: String?,
    @ModelProperty(7, "任务编号")
    var taskCode: String?,
    @ModelProperty(8, "任务描述")
    var taskDesc: String?,
    @ModelProperty(9, "计划编号")
    var planCode: String?,
    @ModelProperty(10, "工作令号")
    var workNo: String?,
    @ModelProperty(11, "批次号")
    var batchNo: String?,
    @ModelProperty(12, "订单编号")
    var orderNo: String?,
    @ModelProperty(14, "质检结论")
    var conclusion: String?,
    @ModelProperty(15, "质检人")
    var inspectorName: String?,
    @ModelProperty(16, "质检时间")
    var inspectorTime: String?,
    @ModelProperty(20, "跟踪部门")
    var traceDepartment: String?,
    @ModelProperty(21, "跟踪时间")
    var traceTime: String?,
    var traceDepartmentId: Int?,
    @ModelProperty(22, "跟踪人")
    var traceUser: String?,
    @ModelProperty(23, "原因分析")
    var causeAnalysis: String?,
    @ModelProperty(24, "解决对策")
    var solution: String?,
    @ModelProperty(25, "状态")
    var status: String?,
    @ModelProperty(26, "优化建议")
    var optimization: String?,
    @ModelProperty(27, "处理周期")
    var processCycle: String?,
    var adverseCauseInfoList: List<QualityNgReasonModel>?,
    var fileList: List<FileInfoModel>?,
    var pictureList: List<FileInfoModel>?,
    var remark: String?,
    var taskId: Int?,
    var traceUserId: Int?,
    var id: Int?
) {
    @Transient
    @ModelProperty(12, "不良原因")
    var ngReasons: String? = null

    fun reInit() {
        ngReasons = (adverseCauseInfoList ?: listOf()).joinToString(";")
        { it.badnessReasonName }
    }

    companion object {
        fun create(): QualityProblemRecordDetailModel {
            return QualityProblemRecordDetailModel(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
            )
        }
    }
}