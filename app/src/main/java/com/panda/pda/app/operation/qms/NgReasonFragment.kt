package com.panda.pda.app.operation.qms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.databinding.FragmentNgReasonBinding
import com.panda.pda.app.databinding.ItemNgReasonBinding
import com.panda.pda.app.operation.qms.data.model.QualityNgReasonModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/10/12
 */
class NgReasonFragment : BaseFragment(R.layout.fragment_ng_reason) {

    private val viewBinding by viewBinding<FragmentNgReasonBinding>()

    private lateinit var ngReasons: List<QualityNgReasonModel>

    private val ngReasonAdapter by lazy {
        Moshi.Builder().build().adapter<List<QualityNgReasonModel>>(
            Types.newParameterizedType(
                List::class.java,
                QualityNgReasonModel::class.java
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        ngReasons =
            ngReasonAdapter.fromJson(
                requireArguments().getString(NG_REASON_ARG_KEY, "")
            ) ?: listOf()
        viewBinding.rvNgList.adapter = object :
            CommonViewBindingAdapter<ItemNgReasonBinding, QualityNgReasonModel>(
                ngReasons.toMutableList()
            ) {
            override fun createBinding(parent: ViewGroup): ItemNgReasonBinding {
                return ItemNgReasonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: QualityNgReasonModel,
                position: Int
            ) {
                holder.itemViewBinding.cbNgReason.text = data.badnessReasonName
                holder.itemViewBinding.cbNgReason.setOnCheckedChangeListener { _, isChecked ->
                    data.isChecked = isChecked
                }
            }
        }

        viewBinding.btnConfirm.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .bindToLifecycle(requireView())
            .subscribe {
                val reasons = ngReasons.filter { it.isChecked }
                if (reasons.isEmpty()) {
                    toast(getString(R.string.ng_reason_empty_message))
                } else {
                    confirm(reasons)
                }
            }
    }

    private fun confirm(reasons: List<QualityNgReasonModel>) {
        setFragmentResult(REQUEST_KEY, Bundle().apply {
            putString(NG_REASON_ARG_KEY, ngReasonAdapter.toJson(reasons))
        })
        navBackListener.invoke(requireView())
    }

    companion object {
        const val REQUEST_KEY = "NG_REASON_REQUEST"
        const val NG_REASON_ARG_KEY = "NG_REASON_ARGS"
    }
}