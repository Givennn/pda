package com.panda.pda.mes.operation.ems.data

import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.common.data.model.IdRequest
import com.panda.pda.mes.common.data.model.TaskMessageCountModel
import com.panda.pda.mes.operation.ems.data.model.*
import com.panda.pda.mes.operation.fms.data.model.TaskReportRequest
import com.panda.pda.mes.operation.qms.data.model.*
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * created by AnJiwei 2021/9/24
 */
interface EquipmentApi {

    /**
     * 分页查询设备模具列表
     *
     * @param facilityType 0=全部(暂定删除) 1=设备 2=模具
     * @param functionStatus 功能状态 1-正常 2-维修 3-保养 4-报废 5-归还，空代表全部
     * @param organizationId 部门id，空代表全部
     * @param searchKey 搜索关键词
     * @param rows 每页数量 (optional)
     * @param page 页码 (optional)
     */
    @GET("pda/ems/facility/list-by-page")
    fun pdaEmsDeviceListByPageGet(
        @retrofit2.http.Query("facilityType") facilityType: Int,
        @retrofit2.http.Query("functionStatus") functionStatus: String?,
        @retrofit2.http.Query("organizationId") organizationId: String?,
        @retrofit2.http.Query("searchKey") keyword: String?,
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,

        ): Single<DataListNode<EquipmentInfoDeviceModel>>

    /**
     * 分页查询工单列表
     *
     * @param facilityType 0=全部(暂定删除) 1=设备 2=模具
     * @param functionStatus 功能状态 1-正常 2-维修 3-保养 4-报废 5-归还，空代表全部
     * @param organizationId 部门id，空代表全部
     * @param searchKey 搜索关键词
     * @param rows 每页数量 (optional)
     * @param page 页码 (optional)
     */
    @GET("pda/ems/work-order/info-list")
    fun pdaEmsWorkOrderListByPageGet(
        @retrofit2.http.Query("facilityType") facilityType: Int,
        @retrofit2.http.Query("status") status: String?,
        @retrofit2.http.Query("orgId") orgId: String?,
        @retrofit2.http.Query("searchKey") keyword: String?,
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,

        ): Single<DataListNode<EquipmentWorkOrderModel>>

    /**
     * 分页查询待处理工单列表
     *
     * @param rows 每页数量 (optional)
     * @param page 页码 (optional)
     */
    @GET("pda/ems/work-order/process-list")
    fun pdaEmsWorkOrderTaskByPageGet(
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,

        ): Single<DataListNode<EquipmentWorkOrderModel>>

    /**
     * 根据设备id查询详情
     * The endpoint is owned by docs service owner
     * @param id 设备id (required)
     */
    @GET("pda/ems/equipment/get-by-id")
    fun pdaEmsDeviceDetailGet(
        @retrofit2.http.Query("id") id: Int,
    ): Single<EquipmentInfoDeviceModel>

    /**
     * 根据工单id查询详情
     * The endpoint is owned by docs service owner
     * @param id 工单id (required)
     */
    @GET("pda/ems/work-order/detail")
    fun pdaEmsWorkOrderDetailGet(
        @retrofit2.http.Query("id") id: String,
    ): Single<EquipmentWorkOrderModel>

    /**
     * 根据模具id查询详情
     * The endpoint is owned by docs service owner
     * @param id 模具id (required)
     */
    @GET("pda/ems/mould/get-by-id")
    fun pdaEmsMouldDetailGet(
        @retrofit2.http.Query("id") id: Int,
    ): Single<EquipmentInfoDeviceModel>

    /**
     * 报工---工单填报
     *
     * The endpoint is owned by docs service owner
     */
    @POST("pda/ems/work-order/add")
    fun pdaEmsTaskSubmitConfirmPost(
        @retrofit2.http.Body request: WorkOrderAddRequest,
    ): Single<Any>

    /**
     * 维保分配
     *
     * The endpoint is owned by docs service owner
     */
    @POST("pda/ems/work-order/dispatch")
    fun pdaEmsWBFenpeiPost(
        @retrofit2.http.Body request: WorkOrderFenpeiRequest,
    ): Single<Any>

    /**
     * 维保完工
     *
     * The endpoint is owned by docs service owner
     */
    @POST("pda/ems/work-order/complete")
    fun pdaEmsWBWangongPost(
        @retrofit2.http.Body request: WorkOrderWangongRequest,
    ): Single<Any>

    /**
     * 维保确认
     *
     * The endpoint is owned by docs service owner
     */
    @POST("pda/ems/work-order/confirm")
    fun pdaEmsWBConfirmPost(
        @retrofit2.http.Body request: WorkOrderWBConfirmRequest,
    ): Single<Any>

    /**
     * 出库确认
     *
     * The endpoint is owned by docs service owner
     */
    @POST("pda/ems/work-order/out-confirm")
    fun pdaEmsOutSotreConfirmPost(
        @retrofit2.http.Body request: WorkOrderOutStoreConfirmRequest,
    ): Single<Any>

    /**
     * 入库提交
     *
     * The endpoint is owned by docs service owner
     */
    @POST("pda/ems/work-order/in-commit")
    fun pdaEmsInSotreSubmitPost(
        @retrofit2.http.Body request: WorkOrderInStoreSubmitRequest,
    ): Single<Any>

    /**
     * 入库确认
     *
     * The endpoint is owned by docs service owner
     */
    @POST("pda/ems/work-order/in-confirm")
    fun pdaEmsInSotreConfirmPost(
        @retrofit2.http.Body request: WorkOrderInStoreConfirmRequest,
    ): Single<Any>

    /**
     * 获取厂商列表
     *
     * The endpoint is owned by docs service owner
     * @param keyword 搜索关键字 (required)
     */
    @GET("pda/ems/work-order/supplier-list")
    fun pdaEmsSupplierListGet(
        @retrofit2.http.Query("keyword") keyword: String?,
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,
    ): Single<DataListNode<EquipmentSupplierChooseModel>>

    /**
     * 获取产品列表
     *
     * The endpoint is owned by docs service owner
     * @param keyword 搜索关键字 (required)
     * @param facilityId 模具id
     */
    @GET("pda/ems/product/list")
    fun pdaEmsProductChooseListGet(
        @retrofit2.http.Query("searchKey") keyword: String?,
        @retrofit2.http.Query("facilityId") facilityId: Int,
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,
    ): Single<DataListNode<EquipmentProductChooseModel>>

    /**
     * 获取产品列表
     *
     * The endpoint is owned by docs service owner
     * @param keyword 搜索关键字 (required)
     */
    @GET("pda/ems/sys/group/detail-list")
    fun pdaEmsPersonChooseListGet(
        @retrofit2.http.Query("id") id: String?,
        @retrofit2.http.Query("key") keyword: String?,
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,
    ): Single<DataListNode<EquipmentPersonChooseModel>>

    /**
     * 获取模具生产数记录
     *
     * The endpoint is owned by docs service owner
     */
    @GET("pda/ems/mould/produce-record-list")
    fun pdaEmsProcedureListGet(
        @retrofit2.http.Query("id") id: String,
    ): Single<DataListNode<EquipmentProcedureModel>>

    /**
     * 获取工单操作记录
     *
     * The endpoint is owned by docs service owner
     */
    @GET("pda/ems/work-order/operate-list")
    fun pdaEmsWorkOrderOperateListGet(
        @retrofit2.http.Query("id") id: String,
    ): Single<DataListNode<EquipmentOperateModel>>

    /**
     * 获取二级部门列表
     *
     * The endpoint is owned by docs service owner
     */
    @GET("pda/ems/org/list")
    fun pdaEmsOrgListGet(
    ): Single<DataListNode<EquipmentOrgModel>>

    /**
     * 条件查询功能类型列表
     *
     * The endpoint is owned by docs service owner
     */
    @GET("pda/ems/function-dict/list")
    fun pdaFunctionTypeListGet(
    ): Single<DataListNode<EquipmentFunctionTypeChooseModel>>

    /**
     * 获取模具出入库记录
     *
     * The endpoint is owned by docs service owner
     */
    @GET("pda/ems/mould/store-order-list")
    fun pdaEmsStoreOrderListGet(
        @retrofit2.http.Query("id") id: Int,
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,
    ): Single<DataListNode<EquipmentInfoMatrixStoreItemModel>>

    /**
     * 获取模具保养记录
     *
     * The endpoint is owned by docs service owner
     */
    @GET("pda/ems/mould/maintain-order-list")
    fun pdaEmsModuleMaintainOrderListGet(
        @retrofit2.http.Query("id") id: Int,
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,
    ): Single<DataListNode<EquipmentInfoMatrixMaintainRepairItemModel>>

    @GET("pda/ems/mould/repair-order-list")
            /**
             * 获取模具维修记录
             *
             * The endpoint is owned by docs service owner
             */
    fun pdaEmsModuleRepairOrderListGet(
        @retrofit2.http.Query("id") id: Int,
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,
    ): Single<DataListNode<EquipmentInfoMatrixMaintainRepairItemModel>>

    /**
     * 获取设备维修记录
     *
     * The endpoint is owned by docs service owner
     */
    @GET("pda/ems/equipment/repair-order-list")
    fun pdaEmsEquipmentRepairOrderListGet(
        @retrofit2.http.Query("id") id: Int,
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,
    ): Single<DataListNode<EquipmentInfoMatrixMaintainRepairItemModel>>

    /**
     * 获取设备保养记录
     *
     * The endpoint is owned by docs service owner
     */
    @GET("pda/ems/equipment/maintain-order-list")
    fun pdaEmsEquipmentMaintainOrderListGet(
        @retrofit2.http.Query("id") id: Int,
        @retrofit2.http.Query("rows") rows: Int = 20,
        @retrofit2.http.Query("page") page: Int = 1,
    ): Single<DataListNode<EquipmentInfoMatrixMaintainRepairItemModel>>

    /**
     * 设备管理/查询待处理任务数量
     *
     * The endpoint is owned by docs service owner
     * @param raw raw paramter (optional)
     */
    @GET("pda/ems/work-order/process-number")
    fun pdaEquipmentTaskMsgCountGet(
    ): Single<List<TaskMessageCountModel>>

}