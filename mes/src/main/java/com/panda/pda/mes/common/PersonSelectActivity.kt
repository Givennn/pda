package com.panda.pda.mes.common

import android.app.ActionBar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.library.android.controls.ContactsIndex.FloatingBarItemDecoration
import com.panda.pda.library.android.controls.ContactsIndex.IndexBar
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.model.PersonModel
import com.panda.pda.mes.databinding.FragmentPersonSelectBinding
import com.panda.pda.mes.databinding.ItemPersonSelectBinding
import java.util.*

class PersonSelectFragment : BaseFragment(R.layout.fragment_person_select) {
    private lateinit var personAdapter: CommonViewBindingAdapter<ItemPersonSelectBinding, PersonModel>
    private var personList = listOf<PersonModel>()
    private val headerList = LinkedHashMap<Int, String>()
    private var operationInfoDialog: PopupWindow? = null
    private var letterHintView: View? = null
    private var layoutManager: LinearLayoutManager? = null

    private var selectedPersonList = LinkedHashMap<Int, PersonModel>()

    private val viewBinding by viewBinding<FragmentPersonSelectBinding>()

//    fun initData() {
//        val intent: Intent = getIntent()
//        try {
//            personList = intent.getParcelableArrayListExtra<PersonBean>(PEOPLE_LIST_KEY)
//            Collections.sort(personList, Comparator<Any> { o1, o2 -> o1.compareTo(o2) })
//        } catch (err: Exception) {
//            personList = ArrayList<PersonBean>()
//        }
//        preOperation()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.rvPersons.apply {
            layoutManager =
                LinearLayoutManager(requireContext()).also { layoutManager = it }
            addItemDecoration(FloatingBarItemDecoration(requireContext(), headerList))
            personAdapter = initAdapter()
            adapter = personAdapter
        }

        viewBinding.navSidebar.setOnTouchingLetterChangedListener(object :
            IndexBar.OnTouchingLetterChangeListener {

            override fun onTouchingLetterChanged(s: String) {
                for (position in headerList.keys) {
                    if (headerList[position] == s) {
                        layoutManager?.scrollToPositionWithOffset(position, 0)
                        return
                    }
                }
            }

            override fun onTouchingStart(s: String) {
                showLetterHintDialog(s)
            }

            override fun onTouchingEnd(s: String?) {
                hideLetterHintDialog()
            }
        })
    }

    fun initView(bundle: Bundle?) {
//        personsRv.setLayoutManager(LinearLayoutManager(this).also { layoutManager = it })
//        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
//        divider.setDrawable(getDrawable(R.drawable.person_select_divider))
//        rvPersons.addItemDecoration(divider)
//        rvPersons.addItemDecoration(FloatingBarItemDecoration(this, headerList))
//        personsRv.setAdapter(initAdapter())
//        navSidebar.setNavigators(ArrayList<E>(headerList.values))
//        navSidebar.setOnTouchingLetterChangedListener(object : IndexBar.OnTouchingLetterChangeListener() {
//            fun onTouchingLetterChanged(s: String) {
////                showLetterHintDialog(s);
//                for (position in headerList.keys) {
//                    if (headerList[position] == s) {
//                        layoutManager.scrollToPositionWithOffset(position, 0)
//                        return
//                    }
//                }
//            }
//
//            fun onTouchingStart(s: String) {
//                showLetterHintDialog(s)
//            }
//
//            fun onTouchingEnd(s: String?) {
//                hideLetterHintDialog()
//            }
//        })
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

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: PersonModel,
                position: Int,
            ) {
                holder.itemViewBinding.cbPerson.apply {
                    text = data.userName
                    check(data.isSelected)
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            selectedPersonList[data.id] = data
                        } else {
                            selectedPersonList.remove(data.id)
                        }
                    }
                }
            }

            override fun onViewRecycled(holder: ViewBindingHolder) {
                super.onViewRecycled(holder)
                holder.itemViewBinding.cbPerson.setOnCheckedChangeListener(null)
            }
        }
    }

    private fun showLetterHintDialog(s: String) {
        if (operationInfoDialog == null) {
            letterHintView = View.inflate(requireContext(), R.layout.dialog_letter_hint, null)
            operationInfoDialog = PopupWindow(letterHintView,
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                false)
            operationInfoDialog!!.isOutsideTouchable = true
        }
        (letterHintView!!.findViewById<View>(R.id.dialog_letter_hint_textview) as TextView).text =
            s
        requireActivity().window?.decorView?.also {
            it.post {
                operationInfoDialog!!.showAtLocation(it.findViewById(
                    R.id.content), Gravity.CENTER, 0, 0)
            }
        }
    }

    private fun hideLetterHintDialog() {
        operationInfoDialog!!.dismiss()
    }

    private fun preOperation() {
        headerList.clear()
        if (personList.isEmpty()) {
            return
        }

        addHeaderToList(0, personList[0].initial)
        for (i in 1 until personList.size) {
            if (!personList[i - 1].initial.equals(personList[i].initial, true)) {
                addHeaderToList(i, personList[i].initial)
            }
        }
    }

    private fun addHeaderToList(index: Int, header: String) {
        headerList[index] = header
    }

    private fun requestPersonList() {
        WebClient.request(CommonApi::class.java).userListAllGet()
            .bindToFragment()
            .subscribe({
                personList = it.dataList
                preOperation()
                updateCheckStatus()
                refreshData()
            }, {})
    }

    private fun updateCheckStatus() {
        selectedPersonList.values.forEach { selectedModel ->
            val person = personList.firstOrNull { listModel -> listModel.id == selectedModel.id }
            if (person != null) {
                person.isSelected = true
            }
        }
    }

    private fun refreshData() {
        viewBinding.navSidebar.setNavigators(headerList.values.toMutableList())
        personAdapter.refreshData(personList)
    }
//    private ArrayList<PersonBean> createTestEntities() {


    //        List<String> dataMeta = Arrays.asList("张飞", "吴孟达", "黑格尔", "刘青云", "石达开", "程心", "罗杰", "张开开", "张关关", "张不开", "张不关", "张辽", "张招", "刘毅", "留言", "成龙", "程成");
    //        ArrayList<PersonBean> result = new ArrayList<>();
    //        for (int i = 1; i != dataMeta.size(); i++) {
    //            result.add(new PersonBean(i * 1000, dataMeta.get(i)));
    //        }
    //        Collections.sort(result, new Comparator<PersonBean>() {
    //            @Override
    //            public int compare(PersonBean o1, PersonBean o2) {
    //                return o1.compareTo(o2);
    //            }
    //        });
    //        return result;
    //    }
}