package com.panda.pda.app.common.adapter

import android.content.Context
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import com.panda.pda.app.databinding.ItemOperationModuleBinding

/**
 * created by AnJiwei 2021/9/15
 */
class ModuleNavigationAdapter(
    @MenuRes menuId: Int,
    context: Context,
    authorityFilter: (MenuItem) -> Boolean,
) :
    RecyclerView.Adapter<ModuleNavigationAdapter.ViewBindingHolder>() {

    private var menu: Menu = PopupMenu(context, null).menu

    private var dataSource: List<MenuItem>

    var navAction: ((navId: Int) -> Unit)? = null

    init {
        MenuInflater(context).inflate(menuId, menu)
        dataSource = menu.iterator().asSequence().toList().filter(authorityFilter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder {
        val binding =
            ItemOperationModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewBindingHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewBindingHolder, position: Int) {
        val menuItem = dataSource[holder.bindingAdapterPosition]
        holder.viewBinding.apply {
            ivIcon.setImageDrawable(menuItem.icon)
            ivTitle.text = menuItem.title
            root.setOnClickListener {
                navAction?.invoke(menuItem.itemId)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
    inner class ViewBindingHolder(val viewBinding: ItemOperationModuleBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

}