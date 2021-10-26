package com.panda.pda.app.task

import android.content.Context
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import com.panda.pda.app.common.data.model.TaskMessageCountModel
import com.panda.pda.app.databinding.ItemOperationModuleBinding
import com.panda.pda.app.databinding.ItemTaskMsgBinding

/**
 * created by AnJiwei 2021/10/25
 */
class TaskMessageNavigationAdapter (@MenuRes menuId: Int,
                                    context: Context,
                                    authorityFilter: (MenuItem) -> Boolean,
) :
RecyclerView.Adapter<TaskMessageNavigationAdapter.ViewBindingHolder>() {

    private var menu: Menu = PopupMenu(context, null).menu

    private var dataSource: List<MenuItem>

    var navAction: ((navId: Int) -> Unit)? = null

    init {
        MenuInflater(context).inflate(menuId, menu)
        dataSource = menu.iterator().asSequence().toList().filter(authorityFilter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder {
        val binding =
            ItemTaskMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewBindingHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewBindingHolder, position: Int) {
        val menuItem = dataSource[holder.bindingAdapterPosition]
        holder.viewBinding.apply {
            ivIcon.setImageDrawable(menuItem.icon)
            tvModuleName.text = menuItem.title
            root.setOnClickListener {
                navAction?.invoke(menuItem.itemId)
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

    }

    inner class ViewBindingHolder(val viewBinding: ItemTaskMsgBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

}