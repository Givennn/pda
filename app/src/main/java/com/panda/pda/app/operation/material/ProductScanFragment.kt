package com.panda.pda.app.operation.material

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.widget.editorActionEvents
import com.jakewharton.rxbinding4.widget.editorActions
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.databinding.FragmentProductScanBinding
import com.panda.pda.app.operation.data.MaterialApi
import com.panda.pda.app.operation.data.model.ProductModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/8/31
 */
class ProductScanFragment : BaseFragment(R.layout.fragment_product_scan) {

    private val viewBinding by viewBinding<FragmentProductScanBinding>()
    private lateinit var materialAction: MaterialAction
    private val materialViewModel by activityViewModels<MaterialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionName = requireArguments().getString(ACTION_KEY)
        if (actionName == null || !MaterialAction.values().map { it.name }.contains(actionName)) {
            toast("未定义物料操作")
            return
        } else {
            materialAction = MaterialAction.valueOf(actionName)
        }
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        viewBinding.tilSearchBar.addOnEditTextAttachedListener {
            Timber.e("text: ${it.editText?.text.toString()}")
        }
        viewBinding.etSearchBar
//            .editorActionEvents { it.actionId == KeyEvent.KEYCODE_ENTER || it.actionId == EditorInfo.IME_ACTION_DONE }
//            .throttleFirst(500, TimeUnit.MILLISECONDS)
//            .bindToLifecycle(requireView())
//            .subscribe { event ->
//                if (event.actionId == EditorInfo.IME_ACTION_DONE) {
//                    onProductInput(event.view.text.toString())
//                } else if (event.actionId == KeyEvent.KEYCODE_ENTER) {
//                    onProductInput(event.view.text.toString())
//                    (event.view as EditText).selectAll()
//                }
//            }
            .setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onProductInput(editText.text.toString())
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                onProductInput(editText.text.toString())
                (editText as EditText).selectAll()
            }
            false
        }

    }

    private fun onProductInput(code: String) {

        val taskId = requireArguments().getInt(TASK_ID, -1)
        val destination = when (materialAction) {
            MaterialAction.Bind -> R.id.materialBindingProductFragment
            MaterialAction.Unbind -> R.id.materialUnbindFragment
            MaterialAction.Replace -> R.id.materialReplaceFragment
        }
        WebClient.request(MaterialApi::class.java)
            .materialTaskQueryBindByProductGet(code, if (taskId == -1) null else taskId)
            .bindToFragment()
            .subscribe({
                materialViewModel.taskBandedMaterialData.postValue(it)
                materialViewModel.scannedProductData.postValue(ProductModel(code))
                navController.navigate(destination)
            }, {})
//        val textFieldHighlightTint = ColorStateList.valueOf(requireContext().getColor(R.color.text_field_search_bar_highlight))
//        viewBinding.tilSearchBar.apply {
//            setStartIconTintList(textFieldHighlightTint)
//            setEndIconTintList(textFieldHighlightTint)
//            boxBackgroundColor = requireContext().getColor(R.color.text_field_search_bar_highlight_background)
//        }
//        val navId = when (materialAction) {
//            MaterialAction.Bind -> R.id.action_productScanBindFragment_to_materialBindingProductFragment
//            MaterialAction.Unbind -> R.id.action_productScanUnbindFragment_to_materialUnbindFragment
//            MaterialAction.Replace -> R.id.action_productScanReplaceFragment_to_materialReplaceFragment
//        }

//        //todo verify product code, api undefined
//        navController.navigate(navId, Bundle().apply {
//            putString(SCANNED_PRODUCT_CODE, viewBinding.etSearchBar.text.toString())
//        })
    }

    enum class MaterialAction {
        Bind,
        Unbind,
        Replace
    }

    companion object {
        const val ACTION_KEY = "material_action"
        const val SCANNED_PRODUCT_CODE = "product_code"
        const val TASK_ID = "task_id"
    }
}