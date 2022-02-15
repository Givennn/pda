package com.panda.pda.mes.operation.fms.material.replace

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.R
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.databinding.FragmentMaterialScanToReplaceBinding
import com.panda.pda.mes.operation.fms.data.MaterialApi
import com.panda.pda.mes.operation.fms.data.model.MaterialModel
import com.panda.pda.mes.operation.fms.data.model.MaterialReplaceBindRequest
import com.panda.pda.mes.operation.fms.material.MaterialViewModel

/**
 * created by AnJiwei 2021/8/31
 */
class ScanToReplaceFragment : BaseFragment(R.layout.fragment_material_scan_to_replace) {
    private val viewBinding by viewBinding<FragmentMaterialScanToReplaceBinding>()
    private val materialViewModel by activityViewModels<MaterialViewModel>()
    private val oldMaterialModel by lazy { materialViewModel.materialData.value!! }
    private val productModel by lazy { materialViewModel.scannedProductData.value!! }
    private var newMaterialModel: MaterialModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
            etSearchBar.setOnEditorActionListener { editText, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onSearching(editText.text.toString())
                } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    onSearching(editText.text.toString())
                    (editText as EditText).selectAll()
                }
                false
            }
            tvMaterialCodeOld.text = oldMaterialModel.materialSerialCode
            tvMaterialDescOld.text = oldMaterialModel.combineInfoStr()
            btnConfirm.setOnClickListener {
                replaceMaterialPost()
            }
        }
    }

    private fun onSearching(materialCode: String) {
        WebClient.request(MaterialApi::class.java)
            .materialTaskQueryMaterialGet(materialCode)
            .bindToFragment()
            .subscribe({ data -> updateNewMaterial(data) }, {})
    }

    private fun updateNewMaterial(data: MaterialModel?) {

        when {
            data == null -> {
                viewBinding.cvNewMaterial.visibility = View.GONE
            }
            data.materialSerialCode.equals(oldMaterialModel.materialSerialCode, true) -> {
                toast(R.string.serial_repeat_alert_message)
                newMaterialModel = null
                return
            }
            else -> {
                viewBinding.tvMaterialCode.text = data.materialSerialCode
                viewBinding.tvMaterialDesc.text = data.combineInfoStr()
                viewBinding.cvNewMaterial.visibility = View.VISIBLE
            }
        }
        newMaterialModel = data
    }

    private fun replaceMaterialPost() {

        if (newMaterialModel == null) {
            toast(R.string.scan_material_code_message)
            return
        }

        WebClient.request(MaterialApi::class.java)
            .pdaFmsTaskMaterialBindChangePost(
                MaterialReplaceBindRequest(
                    productModel.code,
                    newMaterialModel!!.materialSerialCode!!,
                    oldMaterialModel.materialSerialCode!!
                )
            )
            .bindToFragment()
            .subscribe({
                toast(R.string.material_replace_success_message)
                navBackListener.invoke(requireView())
            }, {})

    }
}