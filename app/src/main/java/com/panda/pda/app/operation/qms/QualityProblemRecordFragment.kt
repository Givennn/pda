package com.panda.pda.app.operation.qms

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.StringRes
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.base.retrofit.DataListNode
import com.panda.pda.app.common.adapter.ViewBindingAdapter
import com.panda.pda.app.databinding.FragmentCommonSearchListBinding
import com.panda.pda.app.databinding.FragmentQualityProblemRecordBinding
import com.panda.pda.app.operation.qms.data.model.QualityProblemRecordModel
import io.reactivex.rxjava3.core.Single

class QualityProblemRecordFragment :
    BaseFragment(R.layout.fragment_quality_problem_record) {

    val viewBinding by viewBinding<FragmentQualityProblemRecordBinding>()

    protected val itemListAdapter by lazy { createAdapter() }

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

    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    fun createAdapter(): ViewBindingAdapter<*, QualityProblemRecordModel> {
        TODO("nav 2 commit")
    }

    @get:StringRes
    val titleResId = R.string.quality_problem_record

    @get:StringRes
    val searchBarHintResId: Int? = null


    protected fun onSearching() {
        refreshData()
    }

    protected fun refreshData() {
        TODO("nav 2 commit")
    }
}