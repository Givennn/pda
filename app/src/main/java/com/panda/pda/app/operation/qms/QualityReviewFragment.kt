package com.panda.pda.app.operation.qms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.CommonSearchListFragment
import com.panda.pda.app.common.adapter.ViewBindingAdapter
import com.panda.pda.app.databinding.FrameEmptyViewBinding
import com.panda.pda.app.databinding.ItemQualityReviewBinding
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityTaskModel
import com.panda.pda.app.operation.qms.data.model.QualityTaskModelType
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class QualityReviewFragment : CommonSearchListFragment<QualityTaskModel>() {
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = viewBinding.rvTaskList.layoutManager as? LinearLayoutManager ?: return
        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                WebClient.request(QualityApi::class.java)
                    .pdaQmsDistributeListByPageGet(
                        QualityTaskModelType.Review.code,
                        viewBinding.etSearchBar.text?.toString(),
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

    override fun createAdapter(): ViewBindingAdapter<*, QualityTaskModel> {

        return object : ViewBindingAdapter<ItemQualityReviewBinding, QualityTaskModel>() {
            override fun createBinding(parent: ViewGroup): ItemQualityReviewBinding {
                return ItemQualityReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
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
                holder.itemViewBinding.apply {
                    tvQualityInfo.text = getString(
                        R.string.desc_and_code_formatter,
                        data.qualityDesc,
                        data.qualityCode
                    )
                    tvTaskInfo.text =
                        getString(R.string.desc_and_code_formatter, data.taskDesc, data.taskCode)
                    tvPlanDateSection.text = getString(
                        R.string.time_section_formatter,
                        data.planStartTime,
                        data.planEndTime
                    )
                    tvQualityNumber.text = data.qualityNum.toString()
                    tvInspectNumber.text = data.deliverNum.toString()
                    tvQualityScheme.text = data.qualitySolutionName
                    root.clicks()
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .bindToLifecycle(holder.itemView)
                        .subscribe { showDetail(data) }

                    btnActionTransfer.clicks()
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .bindToLifecycle(holder.itemView)
                        .subscribe { transfer(data) }

                    btnActionReview.clicks()
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .bindToLifecycle(holder.itemView)
                        .subscribe { review(data) }
                }
            }

        }

    }

    private fun showDetail(data: QualityTaskModel) {

    }

    private fun review(data: QualityTaskModel) {

    }

    private fun transfer(data: QualityTaskModel) {

    }

    override fun api(key: String?): Single<DataListNode<QualityTaskModel>> {
        return WebClient.request(QualityApi::class.java)
            .pdaQmsDistributeListByPageGet(QualityTaskModelType.Review.code, key)
            .doFinally { scrollListener.resetState() }
    }

    override val titleResId: Int
        get() = R.string.quality_sign

}