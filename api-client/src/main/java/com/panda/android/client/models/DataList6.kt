/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: mes
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package com.panda.android.client.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.ZonedDateTime

/**
 * @property id 
 * @property operateName 操作人
 * @property operateType 操作类型
 * @property createTime 操作时间
 * @property operateDetail 操作详情
 */
@JsonClass(generateAdapter = true)
data class DataList6(
    @Json(name = "id") @field:Json(name = "id") var id: Int,
    @Json(name = "operateName") @field:Json(name = "operateName") var operateName: String,
    @Json(name = "operateType") @field:Json(name = "operateType") var operateType: String,
    @Json(name = "createTime") @field:Json(name = "createTime") var createTime: ZonedDateTime,
    @Json(name = "operateDetail") @field:Json(name = "operateDetail") var operateDetail: String
)