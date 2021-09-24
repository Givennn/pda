/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: Specs used as a sample for the generator.
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package com.panda.android.client.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @property qualityCode 质检任务编号
 * @property qualityDesc 质检任务描述
 * @property taskCode 任务编号
 * @property taskDesc 任务描述
 * @property bdcQualitySolutionId bdc质检方案id
 * @property qualitySolutionCode 质检方案编码
 * @property qualitySolutionName 质检方案名称
 * @property qualityType 质检类型
 * @property qualityMethod 质检方式
 * @property productCode 产品编码
 * @property productName 产品描述
 * @property deliverNum 送检数量
 * @property qualityNum 需质检数量
 * @property planCode 计划编号
 * @property workNo 工作令号
 * @property batchNo 批次号
 * @property orderNo 订单编号
 * @property planStartTime 计划开始时间
 * @property planEndTime 计划结束时间
 */
@JsonClass(generateAdapter = true)
data class EmptyObject(
    @Json(name = "qualityCode") @field:Json(name = "qualityCode") var qualityCode: String,
    @Json(name = "qualityDesc") @field:Json(name = "qualityDesc") var qualityDesc: String,
    @Json(name = "bdcQualitySolutionId") @field:Json(name = "bdcQualitySolutionId") var bdcQualitySolutionId: Int,
    @Json(name = "qualitySolutionCode") @field:Json(name = "qualitySolutionCode") var qualitySolutionCode: String,
    @Json(name = "qualitySolutionName") @field:Json(name = "qualitySolutionName") var qualitySolutionName: String,
    @Json(name = "productCode") @field:Json(name = "productCode") var productCode: String,
    @Json(name = "deliverNum") @field:Json(name = "deliverNum") var deliverNum: String,
    @Json(name = "taskCode") @field:Json(name = "taskCode") var taskCode: String? = null,
    @Json(name = "taskDesc") @field:Json(name = "taskDesc") var taskDesc: String? = null,
    @Json(name = "qualityType") @field:Json(name = "qualityType") var qualityType: String? = null,
    @Json(name = "qualityMethod") @field:Json(name = "qualityMethod") var qualityMethod: String? = null,
    @Json(name = "productName") @field:Json(name = "productName") var productName: String? = null,
    @Json(name = "qualityNum") @field:Json(name = "qualityNum") var qualityNum: String? = null,
    @Json(name = "planCode") @field:Json(name = "planCode") var planCode: String? = null,
    @Json(name = "workNo") @field:Json(name = "workNo") var workNo: String? = null,
    @Json(name = "batchNo") @field:Json(name = "batchNo") var batchNo: String? = null,
    @Json(name = "orderNo") @field:Json(name = "orderNo") var orderNo: String? = null,
    @Json(name = "planStartTime") @field:Json(name = "planStartTime") var planStartTime: String? = null,
    @Json(name = "planEndTime") @field:Json(name = "planEndTime") var planEndTime: String? = null
)
