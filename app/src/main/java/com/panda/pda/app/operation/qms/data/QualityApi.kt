package com.panda.pda.app.operation.qms.data

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * created by AnJiwei 2021/9/24
 */
interface QualityApi {

//    /**
//     * 执行质检子任务
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pad/qms/qualitySubTask/execute")
//    fun padQmsQualitySubTaskExecutePost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<Map<String, Any?>>
//    /**
//     * 质检子任务操作记录
//     *
//     * The endpoint is owned by docs service owner
//     * @param id 子任务id (optional)
//     * @param raw raw paramter (optional)
//     */
//    @Headers(
//        "Content-Type: text/plain"
//    )
//    @GET("pda/fms/task/operation-record_1632364794858")
//    fun pdaFmsTaskOperationRecord1632364794858Get(
//        @retrofit2.http.Query("id") id: String?,
//        @retrofit2.http.Body raw: List<Byte>?
//    ): Single<WebFmsTaskOperationRecordGetResponse>
//    /**
//     * 根据质检任务id查询质检任务详情
//     *
//     * The endpoint is owned by docs service owner
//     * @param id 质检任务id (required)
//     */
//    @GET("pda/qms/common/detail")
//    fun pdaQmsCommonDetailGet(
//        @retrofit2.http.Query("id") id: String
//    ): Single<EmptyObject43>
//    /**
//     * 查询质检任务操作列表
//     *
//     * The endpoint is owned by docs service owner
//     * @param id 质检任务id (required)
//     */
//    @GET("pda/qms/common/operator-list")
//    fun pdaQmsCommonOperatorListGet(
//        @retrofit2.http.Query("id") id: String
//    ): Single<EmptyObject3>
//    /**
//     * 质检任务派发
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/qms/distribute/distribute")
//    fun pdaQmsDistributeDistributePost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject19>
//    /**
//     * 分页查询质检任务列表
//     *
//     * The endpoint is owned by docs service owner
//     * @param keyword 搜索关键字 (required)
//     * @param modelType 1-质检任务模块，2-质检审核模块，3-质检派发模块，4-质检签收，5-质检执行，6- (required)
//     * @param rows 每页数量 (optional)
//     * @param page 页码 (optional)
//     */
//    @GET("pda/qms/distribute/list-by-page")
//    fun pdaQmsDistributeListByPageGet(
//        @retrofit2.http.Query("keyword") keyword: String,
//        @retrofit2.http.Query("modelType") modelType: String,
//        @retrofit2.http.Query("rows") rows: String?,
//        @retrofit2.http.Query("page") page: String?
//    ): Single<EmptyObject42>
//    /**
//     * 新建质检问题记录
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/qms/qualityProblem/add")
//    fun pdaQmsQualityProblemAddPost(
//        @retrofit2.http.Body root: Root6
//    ): Single<Map<String, Any?>>
//    /**
//     * 编辑质检问题记录
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/qms/qualityProblem/edit")
//    fun pdaQmsQualityProblemEditPost(
//        @retrofit2.http.Body root: Root7
//    ): Single<Map<String, Any?>>
//    /**
//     * 质检问题详情
//     *
//     * The endpoint is owned by docs service owner
//     * @param id  (required)
//     */
//    @GET("pda/qms/qualityProblem/get-by-id")
//    fun pdaQmsQualityProblemGetByIdGet(
//        @retrofit2.http.Query("id") id: String
//    ): Single<EmptyObject32>
//    /**
//     * 质检问题记录列表
//     *
//     * The endpoint is owned by docs service owner
//     * @param keywords 质检任务编号/质检任务描述/质检方案编码/质检方案描述/产品条码/产品编码/产品描述 (required)
//     */
//    @GET("pda/qms/qualityProblem/list-by-page")
//    fun pdaQmsQualityProblemListByPageGet(
//        @retrofit2.http.Query("keywords") keywords: String
//    ): Single<EmptyObject50>
//    /**
//     * 完工质检任务
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/qms/qualitySubTask/finish")
//    fun pdaQmsQualitySubTaskFinishPost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject34>
//    /**
//     * 质检子任务详情
//     *
//     * The endpoint is owned by docs service owner
//     * @param id 质检子任务id (required)
//     */
//    @GET("pda/qms/qualitySubTask/get-by-id")
//    fun pdaQmsQualitySubTaskGetByIdGet(
//        @retrofit2.http.Query("id") id: String
//    ): Single<EmptyObject49>
//    /**
//     * 签收质检任务
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/qms/qualitySubTask/sign")
//    fun pdaQmsQualitySubTaskSignPost(
//        @retrofit2.http.Body root: Map<String, Any?>?
//    ): Single<Map<String, Any?>>
//    /**
//     * 根据质检子任务id查询不良原因列表
//     *
//     * The endpoint is owned by docs service owner
//     * @param id 质检子任务id (required)
//     */
//    @GET("pda/qms/qualityTask/get-badness-list")
//    fun pdaQmsQualityTaskGetBadnessListGet(
//        @retrofit2.http.Query("id") id: String
//    ): Single<EmptyObject27>
//    /**
//     * 根据质检子任务id查询质检项列表
//     *
//     * The endpoint is owned by docs service owner
//     * @param id 质检子任务id (required)
//     */
//    @GET("pda/qms/qualityTask/get-quality-item")
//    fun pdaQmsQualityTaskGetQualityItemGet(
//        @retrofit2.http.Query("id") id: String
//    ): Single<EmptyObject28>
//    /**
//     * 质检任务审核
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/qms/review/review")
//    fun pdaQmsReviewReviewPost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject10>
//    /**
//     * 质检任务转办
//     *
//     * The endpoint is owned by docs service owner
//     * @param id  (required)
//     * @param reviewerId  (required)
//     * @param 备注  (required)
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/qms/review/transfer")
//    fun pdaQmsReviewTransferPost(
//        @retrofit2.http.Query("id") id: String,
//        @retrofit2.http.Query("reviewerId") reviewerId: String,
//        @retrofit2.http.Query("备注") 备注: String,
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject10>
//    /**
//     * 提交质检任务
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/qms/task/commit")
//    fun pdaQmsTaskCommitPost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject14>
//    /**
//     * 接收质检任务
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("pda/qms/task/revice")
//    fun pdaQmsTaskRevicePost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject10>
//    /**
//     * 派发子任务撤销
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("web/pda/distribute/cancel")
//    fun webPdaDistributeCancelPost(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject10>
//    /**
//     * 质检任务转办
//     *
//     * The endpoint is owned by docs service owner
//     * @param root (optional)
//     */
//    @Headers(
//        "Content-Type: application/json"
//    )
//    @POST("web/qms/distribute/transfer_1632379032108")
//    fun webQmsDistributeTransfer1632379032108Post(
//        @retrofit2.http.Body root: EmptyObject
//    ): Single<EmptyObject10>
}