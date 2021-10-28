package com.panda.pda.app.operation.qms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.ConfirmDialogFragment
import com.panda.pda.app.base.extension.toast
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.databinding.FrameEmptyViewBinding
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityTaskModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskModelType
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/9/28
 */
abstract class BaseQualitySearchListFragment<TItemViewBinding : ViewBinding> :
    CommonSearchListFragment<QualityTaskModel>() {

    abstract val qualityTaskModelType: QualityTaskModelType
    protected val qualityViewModel by activityViewModels<QualityViewModel>()

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override val searchBarHintResId: Int?
        get() = R.string.quality_task_search_hint
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = viewBinding.rvTaskList.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                WebClient.request(QualityApi::class.java)
                    .pdaQmsCommonListByPageGet(
                        qualityTaskModelType.code,
                        viewBinding.etSearchBar.text?.toString(),
                        10,
                        page
                    )
                    .bindToFragment()
                    .subscribe({
                        itemListAdapter.addData(it.dataList)
                    }, { })
            }
        }
        viewBinding.rvTaskList.addOnScrollListener(scrollListener)
    }

    override fun createAdapter(): CommonViewBindingAdapter<*, QualityTaskModel> {

        return object : CommonViewBindingAdapter<TItemViewBinding, QualityTaskModel>() {
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
                data: QualityTaskModel,
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
        holder: CommonViewBindingAdapter<TItemViewBinding, QualityTaskModel>.ViewBindingHolder,
        data: QualityTaskModel,
        position: Int
    )

    private fun showDetail(data: QualityTaskModel) {
        Single.zip(
            WebClient.request(QualityApi::class.java).pdaQmsCommonDetailGet(data.id),
            WebClient.request(QualityApi::class.java).pdaQmsCommonOperatorListGet(data.id),
            { info, record -> Pair(info, record) })
            .bindToFragment()
            .subscribe({
                qualityViewModel.qualityDetailInfoData.postValue(it.first)
                qualityViewModel.qualityDetailRecordData.postValue(it.second)
                navController.navigate(R.id.qualityDetailFragment)
            }, {})
    }

    override fun api(key: String?): Single<DataListNode<QualityTaskModel>> {
        return WebClient.request(QualityApi::class.java)
            .pdaQmsCommonListByPageGet(qualityTaskModelType.code, key)
            .doFinally { scrollListener.resetState() }
    }

    override val titleResId: Int
        get() = when (qualityTaskModelType) {
            QualityTaskModelType.Task -> R.string.quality_task
            QualityTaskModelType.Review -> R.string.quality_review
            QualityTaskModelType.Distribute -> R.string.quality_distribute
            QualityTaskModelType.Sign -> R.string.quality_sign
            QualityTaskModelType.Execute -> R.string.quality_execute
            QualityTaskModelType.Finish -> R.string.quality_finish
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