package com.panda.pda.mes.task

import android.content.Context
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import com.panda.pda.mes.R
import com.panda.pda.mes.common.data.model.TaskMessageCountModel
import com.panda.pda.mes.databinding.ItemTaskMsgBinding
import okhttp3.internal.filterList

/**
 * created by AnJiwei 2021/10/25
 */
class TaskMessageNavigationAdapter(
    @MenuRes menuId: Int,
    context: Context,
    authorityFilter: (MenuItem) -> Boolean,
) :
    RecyclerView.Adapter<TaskMessageNavigationAdapter.ViewBindingHolder>() {

    private var menu: Menu = PopupMenu(context, null).menu

    private var dataSource: List<Pair<MenuItem, Int>>

    private val msgKeyMap = mapOf<Int, String>(
        Pair(R.id.taskReceiveFragment, "fmsTaskToReceive"),
        Pair(R.id.taskPrepareFragment, "dispatchOrderToPrepare"),
        Pair(R.id.taskExecuteFragment, "fmsTaskToRun"),
        Pair(R.id.taskReportFragment, "fmsTaskToReport"),
        Pair(R.id.taskFinishFragment, "fmsTaskToFinish"),
        Pair(R.id.quality_task_nav_graph, "qmsTask"),
        Pair(R.id.quality_review_nav_graph, "qmsTaskToAudit"),
        Pair(R.id.quality_distribute_nav_graph, "qmsTaskToIssue"),
        Pair(R.id.quality_sign_nav_graph, "qmsSubTaskToReceive"),
        Pair(R.id.quality_execute_nav_graph, "qmsSubTaskToRun"),
        Pair(R.id.qualityFinishFragment, "qmsTaskToFinish"),
        Pair(R.id.quality_problem_record_nav_graph, "qmsProblem"),
        //ems任务
        Pair(R.id.equipmentTaskFragment, "emsTaskToRun")
    )

    var navAction: ((navId: Int) -> Unit)? = null

    init {
        MenuInflater(context).inflate(menuId, menu)
        dataSource =
            menu.iterator().asSequence().toList().filter(authorityFilter).map { Pair(it, 0) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder {
        val binding =
            ItemTaskMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewBindingHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewBindingHolder, position: Int) {
        val item = dataSource[holder.bindingAdapterPosition]
        holder.viewBinding.apply {
            ivIcon.setImageDrawable(item.first.icon)
            tvModuleName.text = item.first.title
            setBadgeNum(tvBadge, item.second)
            root.setOnClickListener {
                navAction?.invoke(item.first.itemId)
            }
        }
    }


    private fun setBadgeNum(textView: TextView, count: Int): Boolean {
        val badgeText = when {
            count <= 0 -> null
            count > 99 -> "99+"
            else -> count.toString()
        }
        textView.visibility = if (badgeText == null) View.INVISIBLE else View.VISIBLE
        textView.text = badgeText ?: ""
        return badgeText == null
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    fun updateBadge(msg: List<TaskMessageCountModel>?) {
        if (msg == null || msg.isEmpty()) {
            return
        }
        dataSource = dataSource.map { item ->
            val badgeCount =
                msg.firstOrNull { msg -> msg.key == msgKeyMap[item.first.itemId] }?.count

            Pair(item.first, badgeCount ?: 0)
        }
        //过滤维保任务为0时的入口
//        dataSource = dataSource.filter { !(it.first.title == "维保任务" && it.second == 0) }
        dataSource = dataSource.filter { it.second != 0 }
        notifyDataSetChanged()
    }


    inner class ViewBindingHolder(val viewBinding: ItemTaskMsgBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

}