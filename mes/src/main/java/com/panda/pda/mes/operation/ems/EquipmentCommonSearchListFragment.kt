package com.panda.pda.mes.operation.ems

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.StringRes
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.databinding.FragmentEquipmentSelectSearchBinding
import io.reactivex.rxjava3.core.Single

/**
 * created by AnJiwei 2021/9/1
 */
abstract class EquipmentCommonSearchListFragment<TSource> :
    BaseFragment(R.layout.fragment_equipment_select_search) {

    protected val viewBinding by viewBinding<FragmentEquipmentSelectSearchBinding>()

    protected val itemListAdapter by lazy { createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.rvTaskList.adapter = itemListAdapter
        viewBinding.swipe.setOnRefreshListener { refreshData() }
        viewBinding.topAppBar.setTitle(titleResId)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        viewBinding.etSearchBar.setHint(searchBarHintResId ?: R.string.common_search_bar_hint)
        viewBinding.etSearchBar.setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSearching()
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                onSearching()
                (editText as EditText).selectAll()
            }
            false
        }
        viewBinding.tilSearchBarBtn.setOnClickListener{
            onSearching()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    abstract fun createAdapter(): CommonViewBindingAdapter<*, TSource>

    abstract fun api(key: String?): Single<DataListNode<TSource>>
    @get:StringRes
    abstract val titleResId: Int

    @get:StringRes
    protected open val searchBarHintResId: Int? = null


    protected fun onSearching() {
        refreshData()
    }

    protected fun refreshData() {
        api(viewBinding.etSearchBar.text?.toString())
            .bindToFragment()
            .doFinally { viewBinding.swipe.isRefreshing = false }
            .subscribe({ data -> itemListAdapter.refreshData(data.dataList) }, { })
    }
}