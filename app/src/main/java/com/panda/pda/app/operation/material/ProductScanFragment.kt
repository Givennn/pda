package com.panda.pda.app.operation.material

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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
        materialViewModel.materialActionData.observe(viewLifecycleOwner, {
            materialAction = it
        })
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        viewBinding.etSearchBar
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

        val taskId = arguments?.getInt(TASK_ID)
        val destination = when (materialAction) {
            MaterialAction.Bind -> R.id.materialBindingProductFragment
            MaterialAction.Unbind -> R.id.materialUnbindFragment
            MaterialAction.Replace -> R.id.materialReplaceFragment
        }
        WebClient.request(MaterialApi::class.java)
            .materialTaskQueryBindByProductGet(code, taskId)
            .bindToFragment()
            .subscribe({
                materialViewModel.taskBandedMaterialData.postValue(it)
                materialViewModel.scannedProductData.postValue(ProductModel(code))
                navController.navigate(destination)
            }, {})
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