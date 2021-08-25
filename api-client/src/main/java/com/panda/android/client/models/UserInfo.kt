/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: mes
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package com.panda.android.client.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 * @property id id
 * @property userName 姓名
 * @property phoneNumber 手机号
 * @property workCode 用户id
 * @property orgName 组织架构名称
 * @property orgFullName 组织架构全称
 */
@JsonClass(generateAdapter = true)
data class UserInfo(
    @Json(name = "id") @field:Json(name = "id") var id: Int,
    @Json(name = "userName") @field:Json(name = "userName") var userName: String,
    @Json(name = "phoneNumber") @field:Json(name = "phoneNumber") var phoneNumber: String,
    @Json(name = "workCode") @field:Json(name = "workCode") var workCode: String,
    @Json(name = "orgName") @field:Json(name = "orgName") var orgName: String,
    @Json(name = "orgFullName") @field:Json(name = "orgFullName") var orgFullName: String
)