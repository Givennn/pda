package com.panda.pda.mes.common

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.library.android.controls.EndlessRecyclerViewScrollListener
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.extension.getGenericObjectString
import com.panda.pda.mes.base.extension.putGenericObjectString
import com.panda.pda.mes.base.extension.putObjectString
import com.panda.pda.mes.base.retrofit.DataListNode
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.model.PersonModel
import com.panda.pda.mes.databinding.FragmentPersonSelectBinding
import com.panda.pda.mes.databinding.ItemPersonSelectBinding
import com.panda.pda.mes.operation.qms.data.QualityApi
import com.panda.pda.mes.operation.qms.data.model.QualityTaskRecordModel
import com.squareup.moshi.Types
import timber.log.Timber

class PersonSelectFragment : BaseFragment(R.layout.fragment_person_select) {
    private lateinit var personAdapter: CommonViewBindingAdapter<ItemPersonSelectBinding, PersonModel>
    private var personList = mutableListOf<PersonModel>()
//    private var layoutManager: LinearLayoutManager? = null

    private val maxSelectSize = 5
    private var selectedPersonList = HashMap<Int, PersonModel>()

    private val viewBinding by viewBinding<FragmentPersonSelectBinding>()

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val layoutManager = viewBinding.rvPersons.layoutManager as? LinearLayoutManager ?: return

        scrollListener = object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                WebClient.request(CommonApi::class.java)
                    .userListAllGet(viewBinding.etSearchBar.text?.toString(), page)
                    .bindToFragment()
                    .subscribe({
                        personList.addAll(it.dataList)
//                preOperation()
                        updateCheckStatus()
                        refreshData()
                    }, {})
            }
        }
        viewBinding.rvPersons.addOnScrollListener(scrollListener)
        val selectOperator = arguments?.getGenericObjectString<List<PersonModel>>(
            Types.newParameterizedType(
                List::class.java,
                PersonModel::class.java
            )) ?: listOf()
        selectOperator.forEach {
            Timber.e("id:${it.id}, name${it.userName}")
        }
        selectedPersonList.putAll(selectOperator.map { it.id to it })
        notifySelectListChanged()
    }

    private fun commit() {
        setFragmentResult(Companion.PERSON_SELECTED, Bundle().apply {
            putGenericObjectString(selectedPersonList.values.toList(),
                Types.newParameterizedType(
                    List::class.java,
                    PersonModel::class.java
                ))
        })
        navController.popBackStack()
    }


    override fun onResume() {
        super.onResume()
        requestPersonList()
    }

    private fun initAdapter(): CommonViewBindingAdapter<ItemPersonSelectBinding, PersonModel> {
        return object :
            CommonViewBindingAdapter<ItemPersonSelectBinding, PersonModel>(mutableListOf()) {
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
                data: PersonModel,
                position: Int,
            ) {
                holder.itemViewBinding.cbPerson.apply {
                    text = data.userName
                    isChecked = data.isSelected

                    setOnCheckedChangeListener { checkBox, isChecked ->
                        if (isChecked) {
                            if (selectedPersonList.size >= maxSelectSize) {
                                (checkBox as CheckBox).isChecked = false
                            } else {
                                selectedPersonList[data.id] = data
                                notifySelectListChanged()
                            }
                        } else {
                            selectedPersonList.remove(data.id)
                            notifySelectListChanged()
                        }
                    }
                }
            }
        }
    }

    private fun notifySelectListChanged() {
        viewBinding.tvSelectedTitle.text = getString(R.string.person_selected_format,
            selectedPersonList.values.joinToString(",") { it.userName })
        viewBinding.btnConfirm.text =
            getString(R.string.person_select_comfirm_format, selectedPersonList.size, maxSelectSize)
    }


    private fun requestPersonList() {
        WebClient.request(CommonApi::class.java)
            .userListAllGet(viewBinding.etSearchBar.text?.toString())
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
            if (selectedPersonList.containsKey(personModel.id)) {
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
        const val PERSON_SELECTED: String = "person_selected"
    }
}