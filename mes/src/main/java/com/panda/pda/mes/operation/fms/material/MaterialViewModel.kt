package com.panda.pda.mes.operation.fms.material

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.mes.operation.fms.data.model.MaterialModel
import com.panda.pda.mes.operation.fms.data.model.ProductModel
import com.panda.pda.mes.operation.fms.data.model.TaskBandedMaterialModel
import com.panda.pda.mes.operation.fms.data.model.DispatchOrderModel

/**
 * create by AnJiwei 2021/9/5
 */
class MaterialViewModel : ViewModel() {
    val scannedProductData = MutableLiveData<ProductModel>()
    val materialData = MutableLiveData<MaterialModel>()
    val selectedTaskData = MutableLiveData<DispatchOrderModel>()

    val materialActionData = MutableLiveData<ProductScanFragment.MaterialAction>()
    val taskBandedMaterialData = MutableLiveData<TaskBandedMaterialModel>()
}