package com.panda.pda.mes.operation.ems

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.BoolRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.databinding.*
import io.reactivex.rxjava3.core.Single

/**
 * 厂商或者设备多选，支持搜索
 */
@SuppressLint("SupportAnnotationUsage")
abstract class EquipmentCommonChooseSearchListFragment<TSource> :
    BaseFragment(R.layout.fragment_equipment_workorder_outstore_choose_deviceandsupplier) {

    protected val viewBinding by viewBinding<FragmentEquipmentWorkorderOutstoreChooseDeviceandsupplierBinding>()

    protected val itemListAdapter by lazy { createAdapter() }
    var dataList = mutableListOf<TSource>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.rvTaskList.adapter = itemListAdapter
        viewBinding.swipe.setOnRefreshListener { refreshData() }
        viewBinding.topAppBar.setTitle(titleResId)
        //是否展示底部确认按钮，多选时需要展示
        viewBinding.btnConfirm.isVisible=showBottomBtm
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
        viewBinding.tilSearchBarBtn.setOnClickListener {
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
    @get:BoolRes
    abstract val showBottomBtm: Boolean
    @get:StringRes
    protected open val searchBarHintResId: Int? = null


    protected fun onSearching() {
        refreshData()
    }

    protected fun refreshData() {
        api(viewBinding.etSearchBar.text?.toString())
            .bindToFragment()
            .doFinally { viewBinding.swipe.isRefreshing = false }
            .subscribe({ data ->
                dataList = data.dataList as MutableList<TSource>
                itemListAdapter.refreshData(data.dataList)
            }, { })
    }
}