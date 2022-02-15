package com.panda.pda.mes.operation.qms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.ConfirmDialogFragment
import com.panda.pda.mes.base.extension.putGenericObjectString
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.extension.toast
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.CommonSearchListFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.FrameEmptyViewBinding
import com.panda.pda.mes.operation.qms.data.QualityApi
import com.panda.pda.mes.operation.qms.data.model.QualitySubTaskModel
import com.panda.pda.mes.operation.qms.data.model.QualityTaskModelType
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel
import com.squareup.moshi.Types
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/10/29
 */
abstract class BaseQualitySubTaskSearchListFragment<TItemViewBinding : ViewBinding> :
    CommonSearchListFragment<QualitySubTaskModel>() {

    abstract val qualityTaskModelType: QualityTaskModelType

    override val searchBarHintResId: Int?
        get() = R.string.quality_task_search_hint

    override fun createAdapter(): CommonViewBindingAdapter<*, QualitySubTaskModel> {

        return object : CommonViewBindingAdapter<TItemViewBinding, QualitySubTaskModel>() {
            override fun createBinding(parent: ViewGroup): TItemViewBinding {
                return createViewBinding(parent)
            }

            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding {
                return FrameEmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: QualitySubTaskModel,
                position: Int
            ) {
                holder.itemViewBinding.root.clicks()
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .bindToLifecycle(holder.itemView)
                    .subscribe { showDetail(data) }

                onBindViewHolder(holder, data, position)
            }
        }
    }

    abstract fun createViewBinding(parent: ViewGroup): TItemViewBinding

    abstract fun onBindViewHolder(
        holder: CommonViewBindingAdapter<TItemViewBinding, QualitySubTaskModel>.ViewBindingHolder,
        data: QualitySubTaskModel,
        position: Int
    )

    private fun showDetail(data: QualitySubTaskModel) {
        Single.zip(
            WebClient.request(QualityApi::class.java).pdaQmsQualitySubTaskGetByIdGet(data.id),
            WebClient.request(QualityApi::class.java).pdaQmsQualitySubTaskOperationRecordGet(data.id),
            { info, record -> Pair(info, record) })
            .bindToFragment()
            .subscribe({
                navController.navigate(R.id.qualityDetailFragment, Bundle().apply {
                    putObjectString(it.first)
                    putGenericObjectString(
                        it.second,
                        Types.newParameterizedType(
                            DataListNode::class.java,
                            QualityTaskRecordModel::class.java
                        )
                    )
                })
            }, {})
    }

    override fun api(key: String?): Single<DataListNode<QualitySubTaskModel>> {
        return WebClient.request(QualityApi::class.java)
            .pdaQmsCommonSubTaskListGet(qualityTaskModelType.code, key)
    }

    override val titleResId: Int
        get() =
            if (qualityTaskModelType == QualityTaskModelType.Sign) {
                R.string.quality_sign
            } else {
                R.string.quality_execute
            }

    protected fun showActionRequestDialog(
        request: Single<*>,
        dialogTitle: String,
        successMessage: String
    ) {
        val dialog =
            ConfirmDialogFragment().setTitle(dialogTitle)
                .setConfirmButton({ _, _ ->
                    request.bindToFragment()
                        .subscribe({
                            toast(successMessage)
                            refreshData()
                        }, {})
                })
        dialog.show(parentFragmentManager, TAG)
    }

}