package com.panda.pda.mes.operation.bps

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getGenericObjectString
import com.panda.pda.mes.base.extension.putGenericObjectString
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.operation.bps.data.model.EquipmentModel
import com.panda.pda.mes.common.data.model.PersonModel
import com.panda.pda.mes.databinding.FragmentPersonSelectBinding
import com.panda.pda.mes.databinding.ItemPersonSelectBinding
import com.panda.pda.mes.operation.bps.data.MainPlanApi
import com.squareup.moshi.Types
import timber.log.Timber


/**
 * created by AnJiwei 2022/9/24
 */
class EquipmentSelectFragment : BaseFragment(R.layout.fragment_person_select) {
    private lateinit var personAdapter: CommonViewBindingAdapter<ItemPersonSelectBinding, EquipmentModel>
    private var personList = mutableListOf<EquipmentModel>()
//    private var layoutManager: LinearLayoutManager? = null

    private var maxSelectSize = 5
    private val defaultSelectSize = 5
    private var selectedequipmentList = HashMap<Int, EquipmentModel>()

    private val viewBinding by viewBinding<FragmentPersonSelectBinding>()

//    private lateinit var scrollListener: EndlessRecyclerViewScrollListener


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        maxSelectSize = arguments?.getInt(MAX_SELECT_SIZE, defaultSelectSize) ?: defaultSelectSize
        personAdapter = initAdapter()
        viewBinding.rvPersons.adapter = personAdapter
        viewBinding.btnConfirm.setOnClickListener {
            commit()
        }

        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener(it) }
        viewBinding.etSearchBar.setOnEditorActionListener { editText, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                requestPersonList()
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                requestPersonList()
                (editText as EditText).selectAll()
            }
            false
        }

//        val layoutManager = viewBinding.rvPersons.layoutManager as? LinearLayoutManager ?: return

//        scrollListener = object :
//            EndlessRecyclerViewScrollListener(layoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                WebClient.request(CommonApi::class.java)
//                    .userListAllGet(viewBinding.etSearchBar.text?.toString(), page)
//                    .bindToFragment()
//                    .subscribe({
//                        personList.addAll(it.dataList)
//                        updateCheckStatus()
//                        refreshData()
//                    }, {})
//            }
//        }
//        viewBinding.rvPersons.addOnScrollListener(scrollListener)
        val selectEquipmentModel = arguments?.getGenericObjectString<List<EquipmentModel>>(
            Types.newParameterizedType(
                List::class.java,
                EquipmentModel::class.java
            )) ?: listOf()
        selectEquipmentModel.forEach {
            Timber.e("id:${it.id}, name${it.equipmentDesc}")
        }
        selectedequipmentList.putAll(selectEquipmentModel.map { it.id to it })
        notifySelectListChanged()
    }

    private fun commit() {
        setFragmentResult(EQUIPMENT_SELECTED, Bundle().apply {
            putGenericObjectString(selectedequipmentList.values.toList(),
                Types.newParameterizedType(
                    List::class.java,
                    EquipmentModel::class.java
                ))
        })
        navController.popBackStack()
    }


    override fun onResume() {
        super.onResume()
        requestPersonList()
    }

    private fun initAdapter(): CommonViewBindingAdapter<ItemPersonSelectBinding, EquipmentModel> {
        return object :
            CommonViewBindingAdapter<ItemPersonSelectBinding, EquipmentModel>(mutableListOf()) {
            override fun createBinding(parent: ViewGroup): ItemPersonSelectBinding {
                return ItemPersonSelectBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun onViewRecycled(holder: ViewBindingHolder) {
                super.onViewRecycled(holder)
                holder.itemViewBinding.cbPerson.setOnCheckedChangeListener(null)
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: EquipmentModel,
                position: Int,
            ) {
                holder.itemViewBinding.cbPerson.apply {
                    text = data.equipmentDesc
                    isChecked = data.isSelected

                    setOnCheckedChangeListener { checkBox, isChecked ->
                        if (isChecked) {
                            if (selectedequipmentList.size >= maxSelectSize) {
                                (checkBox as CheckBox).isChecked = false
                            } else {
                                selectedequipmentList[data.id] = data
                                notifySelectListChanged()
                            }
                        } else {
                            selectedequipmentList.remove(data.id)
                            notifySelectListChanged()
                        }
                    }
                }
            }
        }
    }

    private fun notifySelectListChanged() {
        viewBinding.tvSelectedTitle.text = getString(R.string.person_selected_format,
            selectedequipmentList.values.joinToString(",") { it.equipmentDesc })
        viewBinding.btnConfirm.text =
            getString(R.string.person_select_comfirm_format, selectedequipmentList.size, maxSelectSize)
    }


    private fun requestPersonList() {
        WebClient.request(MainPlanApi::class.java)
            .equipmentAllGet(viewBinding.etSearchBar.text?.toString())
            .bindToFragment()
            .subscribe({
                personList = it.dataList.toMutableList()
//                preOperation()
                updateCheckStatus()
                refreshData()
            }, {})
    }

    private fun updateCheckStatus() {
        personList.forEachIndexed { index, personModel ->
            if (selectedequipmentList.containsKey(personModel.id)) {
                personModel.isSelected = true
            }
            personAdapter.notifyItemChanged(index)
        }
    }

    private fun refreshData() {
//        viewBinding.navSidebar.setNavigators(headerList.values.toMutableList())
        personAdapter.refreshData(personList)
//        notifySelectListChanged()
    }

    companion object {
        const val EQUIPMENT_SELECTED: String = "equipment_selected"
        const val MAX_SELECT_SIZE: String = "max_select_size"
    }
}