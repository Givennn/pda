package com.panda.pda.app.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import com.panda.pda.app.databinding.ItemOperationModuleBinding

/**
 * created by AnJiwei 2021/9/15
 */
class ModuleNavigationAdapter(@MenuRes menuId: Int, context: Context) :
    RecyclerView.Adapter<ModuleNavigationAdapter.ViewBindingHolder>() {

    private var menu: Menu = PopupMenu(context, null).menu

    var navAction: ((navId: Int) -> Unit)? = null

    init {
        MenuInflater(context).inflate(menuId, menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder {
        val binding =
            ItemOperationModuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewBindingHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewBindingHolder, position: Int) {
        val menuItem = menu.getItem(holder.bindingAdapterPosition)
        holder.viewBinding.apply {
            ivIcon.setImageDrawable(menuItem.icon)
            ivTitle.text = menuItem.title
            root.setOnClickListener {
                navAction?.invoke(menuItem.itemId)
            }
        }
    }

    override fun getItemCount(): Int {
        return menu.size()
    }

    inner class ViewBindingHolder(val viewBinding: ItemOperationModuleBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}