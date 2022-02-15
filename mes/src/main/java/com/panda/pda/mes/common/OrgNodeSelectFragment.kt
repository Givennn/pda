package com.panda.pda.mes.common

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding4.view.clicks
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.base.retrofit.WebClient
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.model.OrgNodeModel
import com.panda.pda.mes.databinding.FragmentOrgNodeSelectBinding
import com.panda.pda.mes.databinding.ItemOrgNodeBinding
import com.panda.pda.mes.databinding.ItemOrgSelectTitleBinding
import com.trello.rxlifecycle4.kotlin.bindToLifecycle
import java.util.concurrent.TimeUnit

/**
 * created by AnJiwei 2021/10/8
 */
class OrgNodeSelectFragment : BaseFragment(R.layout.fragment_org_node_select) {

    private val viewBinding by viewBinding<FragmentOrgNodeSelectBinding>()

    private lateinit var titleAdapter: CommonViewBindingAdapter<*, OrgNodeModel>
    private lateinit var orgNodeAdapter: CommonViewBindingAdapter<*, OrgNodeModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(TITLE_KEY)
        if (title != null) {
            viewBinding.topAppBar.title = title
        }
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        setupTitleRv()
        setupNodeRv()
    }

    override fun onResume() {
        super.onResume()
        getOrgNodes()
    }

    private fun onPersonSelected(node: OrgNodeModel) {
        if (titleAdapter.dataSource.size > 1) {
            node.department = titleAdapter.dataSource.last()
        }
        setFragmentResult(ORG_NODE_RESULT, Bundle().apply {
            putSerializable(PERSON_SELECT_KEY, node)
        })
        navBackListener.invoke(requireView())
    }

    private fun getOrgNodes(node: OrgNodeModel? = null) {
        WebClient.request(CommonApi::class.java)
            .userListOrgNodeGet(node?.nodeId ?: "ORG_1")
            .bindToFragment()
            .subscribe({
                orgNodeAdapter.refreshData(it.dataList)
                if (node != null) {
                    node.childNodeList = it.dataList
                    titleAdapter.addData(node)
                    viewBinding.tvOrgTitles.scrollToPosition(titleAdapter.itemCount - 1)
                } else {
                    val rootNode = OrgNodeModel(-1, it.dataList, "ORG_1", 0, "部门选择", 1, "")
                    titleAdapter.addData(rootNode)
                }
            }, {})
    }

    private fun setupNodeRv() {
        orgNodeAdapter = object : CommonViewBindingAdapter<ItemOrgNodeBinding, OrgNodeModel>() {
            override fun createBinding(parent: ViewGroup): ItemOrgNodeBinding {
                return ItemOrgNodeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            override fun onBindViewHolderWithData(
                holder: ViewBindingHolder,
                data: OrgNodeModel,
                position: Int
            ) {

                holder.itemViewBinding.tvName.text = data.nodeName
                holder.itemViewBinding.ivIcon.visibility =
                    if (data.isOrgNode) View.VISIBLE else View.INVISIBLE

                holder.itemViewBinding.root.clicks()
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .bindToLifecycle(holder.itemView)
                    .subscribe {
                        if (data.isOrgNode) {
                            getOrgNodes(data)
                        } else {
                            onPersonSelected(data)
                        }
                    }
            }

        }
        viewBinding.tvOrgNode.adapter = orgNodeAdapter
    }

    private fun setupTitleRv() {
        titleAdapter =
            object : CommonViewBindingAdapter<ItemOrgSelectTitleBinding, OrgNodeModel>() {
                override fun createBinding(parent: ViewGroup): ItemOrgSelectTitleBinding {
                    return ItemOrgSelectTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: OrgNodeModel,
                    position: Int
                ) {
                    val itemPosition = holder.bindingAdapterPosition
                    holder.itemViewBinding.tvOrgTitle
                        .apply {
                            text = data.nodeName
                            isEnabled = itemPosition != itemCount - 1
                        }.clicks()
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .bindToLifecycle(holder.itemView)
                        .subscribe {
                            refreshData(dataSource.dropLast(itemCount - itemPosition - 1))
                            orgNodeAdapter.refreshData(data.childNodeList)
                        }
                }

            }
        viewBinding.tvOrgTitles.apply {
            adapter = titleAdapter
            addItemDecoration(object : DividerItemDecoration(
                requireContext(),
                RecyclerView.HORIZONTAL
            ) {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    if (parent.getChildAdapterPosition(view) == state.itemCount - 1)
                        outRect.setEmpty()
                    else
                        super.getItemOffsets(outRect, view, parent, state)
                }
            }.apply {
                setDrawable(
                    resources.getDrawable(
                        R.drawable.inset_org_select_title_divider,
                        null
                    )
                )
            })
        }
    }

    companion object {
        const val TITLE_KEY = "ORG_SELECT_TITLE"
        const val ORG_NODE_RESULT = "ORG_NODE_RESULT"
        const val PERSON_SELECT_KEY = "ORG_PERSON_SELECTED"
    }
}