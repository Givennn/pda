package com.panda.pda.app.operation.qms.data.model

import com.panda.pda.app.common.ModelProperty
import com.panda.pda.app.common.data.model.FileInfoModel
import com.panda.pda.app.operation.qms.data.model.QualityDetailModel.Companion.QualityDetailTag

/**
 * created by AnJiwei 2021/9/26
 */
data class QualityProblemRecordDetailModel(
    @ModelProperty(1, "质检问题记录")
    val id: Int,
    @ModelProperty(2, "产品条码")
    val productBarCode: String,
    @ModelProperty(3, "产品编码")
    val productCode: String,
    @ModelProperty(4, "产品描述")
    val productName: String,
    @ModelProperty(5, "发生地")
    val occurrencePlace: String,
    @ModelProperty(6, "质检任务编号")
    val qualityCode: String,
    @ModelProperty(7, "任务编号")
    val taskCode: String,
    @ModelProperty(8, "任务描述")
    val taskDesc: String,
    @ModelProperty(9, "计划编号")
    val planCode: String,
    @ModelProperty(10, "工作令号")
    val workNo: String,
    @ModelProperty(11, "批次号")
    val batchNo: String,
    @ModelProperty(12, "订单编号")
    val orderCode: String,
    @ModelProperty(14, "质检结论")
    val conclusion: String,
    @ModelProperty(15, "质检人")
    val inspector: String,
    @ModelProperty(16, "质检时间")
    val inspectorTime: String,
    @ModelProperty(20, "跟踪部门")
    val traceDepartment: String,
    @ModelProperty(21, "跟踪时间")
    val traceTime: String,
    val traceDepartmentId: Int,
    @ModelProperty(22, "跟踪人")
    val traceUser: String,
    @ModelProperty(23, "原因分析")
    val causeAnalysis: String,
    @ModelProperty(24, "解决对策")
    val solution: String,
    @ModelProperty(25, "状态")
    val status: String,
    @ModelProperty(26, "优化建议")
    val optimization: String,
    @ModelProperty(27, "处理周期")
    val processCycle: String,
    val adverseCauseInfoList: List<QualityNgReasonModel>,
    val createId: String,
    val createName: String,
    val createTime: String,
    val fileList: List<FileInfoModel>,
    val orderNo: String,
    val pictureList: List<FileInfoModel>,
    val problemCode: String,
    val productDesc: String,
    val remark: String,
    val taskId: Int,
    val traceUserId: Int,
    val updateId: String,
    val updateName: String,
) {
//    @ModelProperty(12, "不良原因")
    val ngReasons: String get() = adverseCauseInfoList.joinToString(";") { it.badnessReasonName }
}