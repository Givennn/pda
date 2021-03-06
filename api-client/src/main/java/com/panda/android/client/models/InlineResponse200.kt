/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: Specs used as a sample for the generator.
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
data class InlineResponse200(
    @Json(name = "code") @field:Json(name = "code") var code: Int? = null,
    @Json(name = "message") @field:Json(name = "message") var message: String? = null,
    @Json(name = "data") @field:Json(name = "data") var `data`: Map<String, Any?>? = null
)
