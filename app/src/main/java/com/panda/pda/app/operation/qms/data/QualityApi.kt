package com.panda.pda.app.operation.qms.data

import com.panda.pda.app.base.retrofit.BaseResponse
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.common.data.model.IdRequest
import com.panda.pda.app.operation.fms.data.model.ProductModel
import com.panda.pda.app.operation.qms.data.model.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * created by AnJiwei 2021/9/24
 */
interface QualityApi {

    /**
     * 分页查询质检任务列表
     *
     * The endpoint is owned by docs service owner
     * @param keyword 搜索关键字 (required)
     * @param modelType 1-质检任务模块，2-质检审核模块，3-质检派发模块，4-质检签收，5-质检执行，6- (required)
     * @param rows 每页数量 (optional)
     * @param page 页码 (optional)
     */
    @GET("pda/qms/common/list-by-page")
    fun pdaQmsCommonListByPageGet(
        @retrofit2.http.Query("modelType") modelType: Int,
        @retrofit2.http.Query("keyword") keyword: String?,
        @retrofit2.http.Query("rows") rows: Int = 10,
        @retrofit2.http.Query("page") page: Int = 1
    ): Single<DataListNode<QualityTaskModel>>

    /**
     * 获取子任务列表
     *
     * The endpoint is owned by docs service owner
     * @param keyword 搜索关键字 (required)
     * @param modelType 1-质检签收，2-质检执行
     */
    @GET("pda/qms/common/sub-task-list")
    fun pdaQmsCommonSubTaskListGet(
        @retrofit2.http.Query("modelType") modelType: Int,
        @retrofit2.http.Query("keyword") keyword: String?,
    ): Single<DataListNode<QualitySubTaskModel>>

    /**
     * 根据质检任务id查询质检任务详情
     * The endpoint is owned by docs service owner
     * @param id 质检任务id (required)
     */
    @GET("pda/qms/common/detail")
    fun pdaQmsCommonDetailGet(
        @retrofit2.http.Query("id") id: Int
    ): Single<QualityDetailModel>

    /**
     * 查询质检任务操作列表
     * The endpoint is owned by docs service owner
     * @param id 质检任务id (required)
     */
    @GET("pda/qms/common/operator-list")
    fun pdaQmsCommonOperatorListGet(
        @retrofit2.http.Query("id") id: Int
    ): Single<DataListNode<QualityTaskRecordModel>>

    /**
     * 质检任务派发
     * The endpoint is owned by docs service owner
     * @param body (optional)
     */
    @POST("pda/qms/qualityTask/distribute")
    fun pdaQmsDistributeDistributePost(
        @retrofit2.http.Body body: QualityTaskDistributeRequest
    ): Single<Any>

    /**
     * 新建质检问题记录
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/qms/qualityProblem/add")
    fun pdaQmsQualityProblemAddPost(
        @retrofit2.http.Body body: QualityProblemRecordDetailModel
    ): Single<Any>

    /**
     * 编辑质检问题记录
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/qms/qualityProblem/edit")
    fun pdaQmsQualityProblemEditPost(
        @retrofit2.http.Body body: QualityProblemRecordDetailModel
    ): Single<Any>

    /**
     * 质检问题详情
     * The endpoint is owned by docs service owner
     * @param id  (required)
     */
    @GET("pda/qms/qualityProblem/get-by-id")
    fun pdaQmsQualityProblemGetByIdGet(
        @retrofit2.http.Query("id") id: Int
    ): Single<QualityProblemRecordDetailModel>

    /**
     * 质检问题记录列表
     * The endpoint is owned by docs service owner
     * @param keywords 质检任务编号/质检任务描述/质检方案编码/质检方案描述/产品条码/产品编码/产品描述 (required)
     */
    @GET("pda/qms/qualityProblem/list-by-page")
    fun pdaQmsQualityProblemListByPageGet(
        @retrofit2.http.Query("keywords") keywords: String
    ): Single<DataListNode<QualityProblemRecordModel>>

    /**
     * 派发子任务撤销
     */
    @POST("pda/qms/qualityTask/distribute-cancel")
    fun qualityDistributeCancel(
        @retrofit2.http.Body body: QualityTaskDistributeCancelRequest
    ): Single<Any>

    /**
     * 完工质检任务
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/qms/qualityTask/finish")
    fun pdaQmsQualityTaskFinishPost(
        @retrofit2.http.Body body: IdRequest
    ): Single<Any>

    /**
     * 质检子任务详情
     * The endpoint is owned by docs service owner
     * @param id 质检子任务id (required)
     */
    @GET("pda/qms/qualitySubTask/get-by-id")
    fun pdaQmsQualitySubTaskGetByIdGet(
        @retrofit2.http.Query("id") id: Int
    ): Single<QualitySubTaskDetailModel>

    /**
     * 签收质检任务
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/qms/qualitySubTask/sign")
    fun pdaQmsQualitySubTaskSignPost(
        @retrofit2.http.Body body: IdRequest
    ): Single<Any>

    /**
     * 根据质检子任务id查询不良原因列表
     * The endpoint is owned by docs service owner
     * @param id 质检子任务id (required)
     */
    @GET("pda/qms/qualitySubTask/get-badness-list")
    fun pdaQmsQualitySubTaskGetBadnessListGet(
        @retrofit2.http.Query("id") id: Int
    ): Single<DataListNode<QualityNgReasonModel>>


    /**
     * 查询所有启用状态的不良原因
     * The endpoint is owned by docs service owner
     * @param id 质检子任务id (required)
     */
    @GET("pda/qms/qualityProblem/get-badness-reason")
    fun pdaQmsQualityProblemGetBadnessListGet(
    ): Single<DataListNode<QualityNgReasonModel>>

    /**
     * 根据质检子任务id查询质检项列表
     * The endpoint is owned by docs service owner
     * @param id 质检子任务id (required)
     */
    @GET("pda/qms/qualitySubTask/get-quality-item")
    fun pdaQmsQualityTaskGetQualityItemGet(
        @retrofit2.http.Query("id") id: Int
    ): Single<DataListNode<QualityInspectItemModel>>

    /**
     * 质检任务审核
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/qms/qualityTask/review")
    fun pdaQmsReviewReviewPost(
        @retrofit2.http.Body body: QualityTaskReviewRequest
    ): Single<Any>

    /**
     * 质检任务转办
     * The endpoint is owned by docs service owner
     * @param id  (required)
     * @param reviewerId  (required)
     * @param 备注  (required)
     * @param root (optional)
     */
    @POST("pda/qms/qualityTask/review-transfer")
    fun pdaQmsReviewTransferPost(
        @Body body: QualityTaskTransferRequest
    ): Single<Any>

    /**
     * 派发质检任务转办
     * The endpoint is owned by docs service owner
     * @param id  (required)
     * @param reviewerId  (required)
     * @param 备注  (required)
     * @param root (optional)
     */
    @POST("pda/qms/qualityTask/distribute-transfer")
    fun pdaQmsQualityTaskDistributeTransferPost(
        @Body body: QualityTaskDistributeTransferRequest
    ): Single<Any>

    /**
     * 提交质检任务
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/qms/qualityTask/commit")
    fun pdaQmsTaskCommitPost(
        @retrofit2.http.Body body: QualityTaskCommitRequest
    ): Single<Any>


    /**
     * 接收质检任务
     * The endpoint is owned by docs service owner
     * @param root (optional)
     */
    @POST("pda/qms/qualityTask/receive")
    fun pdaQmsTaskReceivePost(
        @retrofit2.http.Body body: IdRequest
    ): Single<Any>

    /**
     * 根据质检子任务id查询质检方案的结论判定选项
     */
    @GET("pda/qms/qualitySubTask/get-conclusion-option")
    fun pdaQmsQualitySubTaskGetConclusionOptionGet(
        @retrofit2.http.Query("id") id: Int
    ): Single<DataListNode<String>>

    /**
     * 执行质检子任务
     */
    @POST("pda/qms/qualitySubTask/execute")
    fun pdaQmsQualitySubTaskExecutePost(
        @retrofit2.http.Body body: QualityTaskExecuteRequest
    ): Single<Any>

    /**
     * 质检子任务操作记录
     */
    @GET("pda/qms/qualitySubTask/operation-record")
    fun pdaQmsQualitySubTaskOperationRecordGet(
        @retrofit2.http.Query("id") id: Int
    ): Single<DataListNode<QualityTaskRecordModel>>

    /**
     * 根据产品条码查询关联内容
     */
    @GET("pda/qms/qualityProblem/get-by-productBarCode")
    fun pdaQmsQualityProblemGetByProductBarCodeGet(
        @retrofit2.http.Query("productBarCode") barCode: String
    ): Single<ProductInfoModel>
}