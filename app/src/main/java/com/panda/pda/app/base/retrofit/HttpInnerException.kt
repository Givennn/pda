package com.panda.pda.app.base.retrofit

/**
 * created by AnJiwei 2020/10/29
 */
class HttpInnerException(val code: Int, message: String?) : Exception(message) {

    constructor(response: BaseResponse<*>) : this(response.code, response.message)
}