package com.panda.pda.app.operation.qms.quality_problem_record

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.retrofit.WebClient
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.databinding.FragmentQualityProblemRecordBinding
import com.panda.pda.app.databinding.FrameEmptyViewBinding
import com.panda.pda.app.databinding.ItemQualityProblemRecordBinding
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.QualityApi
import com.panda.pda.app.operation.qms.data.model.QualityProblemRecordModel
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class QualityProblemRecordFragment :
    BaseFragment(R.layout.fragment_quality_problem_record) {

    private val viewBinding by viewBinding<FragmentQualityProblemRecordBinding>()

    private val viewModel by activityViewModels<QualityViewModel>()

    private val itemListAdapter by lazy { createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.commonLayout
            .apply {
                rvTaskList.adapter = itemListAdapter
                swipe.setOnRefreshListener { refreshData() }
                topAppBar.setTitle(titleResId)
                topAppBar.setNavigationOnClickListener { navBackListener(it) }
                etSearchBar.setHint(searchBarHintResId ?: R.string.common_search_bar_hint)
                etSearchBar.setOnEditorActionListener { editText, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        onSearching()
                    } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                        onSearching()
                        (editText as EditText).selectAll()
                    }
                    false
                }
            }

        viewBinding.floatingActionButton.clicks()
            .bindToLifecycle(requireView())
            .subscribe {
                addRecord()
            }

    }

    private fun addRecord() {
        navController.navigate(R.id.action_qualityProblemRecordFragment_to_problemRecordEditFragment)
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    fun createAdapter(): CommonViewBindingAdapter<ItemQualityProblemRecordBinding, QualityProblemRecordModel> {
        return object :
            CommonViewBindingAdapter<ItemQualityProblemRecordBinding, QualityProblemRecordModel>() {
            override fun createBinding(parent: ViewGroup): ItemQualityProblemRecordBinding {
                return ItemQualityProblemRecordBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun createEmptyViewBinding(parent: ViewGroup): ViewBinding? {
                return FrameEmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: QualityProblemRecordModel,
                position: Int
            ) {
                holder.itemViewBinding.apply {
                    tvProductInfo.text = getString(
                        R.string.desc_and_code_formatter,
                        data.productName,
                        "${data.productBarCode} ${data.productCode}"
                    )
                    tvQualityProblemInfo.text =
                        getString(
                            R.string.desc_and_code_formatter,
                            data.occurrencePlace,
                            data.problemCode
                        )
                    tvFollower.text = data.traceUser
                    tvInspectTaskCode.text = data.qualityCode
//                    tvQualityScheme.text = data.qualitySolutionName
                    tvQualityResult.text = data.conclusion
                    btnActionEdit.clicks()
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .bindToLifecycle(holder.itemView)
                        .subscribe { editRecord(data) }
                    root.clicks()
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .bindToLifecycle(holder.itemView)
                        .subscribe { showRecordDetail(data) }
                }
            }
        }
    }

    private fun showRecordDetail(data: QualityProblemRecordModel) {
        Single.zip(
            WebClient.request(QualityApi::class.java)
                .pdaQmsQualityProblemGetByIdGet(data.id),
            WebClient.request(QualityApi::class.java)
                .pdaQmsCommonOperatorListGet(data.taskId)
        ) { detail, record -> Pair(detail, record) }
            .bindToFragment()
            .subscribe({
                viewModel.problemRecordDetailData.postValue(it.first)
                viewModel.qualityDetailRecordData.postValue(it.second)
                navController.navigate(R.id.action_qualityProblemRecordFragment_to_problemDetailFragment)
            }, {})
    }

    private fun editRecord(data: QualityProblemRecordModel) {
        WebClient.request(QualityApi::class.java)
            .pdaQmsQualityProblemGetByIdGet(data.id)
            .bindToFragment()
            .subscribe({
                navController.navigate(
                    R.id.action_qualityProblemRecordFragment_to_problemRecordEditFragment,
                    Bundle().apply {
                        putString(
                            ProblemRecordEditFragment.DETAIL_KEY,
                            ProblemRecordEditFragment.getProblemRecordJsonAdapter().toJson(it)
                        )
                    })
            }, {})

    }

    @get:StringRes
    val titleResId = R.string.quality_problem_record

    @get:StringRes
    val searchBarHintResId: Int? = null


    private fun onSearching() {
        refreshData()
    }

    private fun refreshData() {
        WebClient.request(QualityApi::class.java)
            .pdaQmsQualityProblemListByPageGet(viewBinding.commonLayout.etSearchBar.text.toString())
            .bindToFragment()
            .subscribe({
                itemListAdapter.refreshData(it.dataList)
                viewBinding.commonLayout.swipe.isRefreshing = false
            }, {})
    }
}