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
 * @property code 
 * @property message 
 * @property `data`
 */
@JsonClass(generateAdapter = true)
data class PdaFmsTaskGetByIdGetResponse(
    @Json(name = "code") @field:Json(name = "code") var code: String,
    @Json(name = "message") @field:Json(name = "message") var message: String,
    @Json(name = "data") @field:Json(name = "data") var `data`: Data14
)
