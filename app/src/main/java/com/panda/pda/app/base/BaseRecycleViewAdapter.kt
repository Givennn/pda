package com.panda.pda.app.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * created by AnJiwei 2021/3/15
 */
abstract class BaseRecycleViewAdapter<TBinding : ViewBinding, TSource>(private val dataSource: MutableList<TSource>) :
    RecyclerView.Adapter<BaseRecycleViewAdapter<TBinding, TSource>.ViewBindingHolder>() {

    protected abstract fun createBinding(parent: ViewGroup): TBinding

    open fun createEmptyViewBinding(parent: ViewGroup): ViewBinding? {
        return null
    }

    protected abstract fun onBindViewHolderWithData(
        holder: ViewBindingHolder,
        data: TSource,
        position: Int,
    )

    private var hasLoadedData = false

    fun refreshData(newSource: List<TSource>) {
        dataSource.clear()
        dataSource.addAll(newSource)
        notifyDataSetChanged()
        hasLoadedData = true
    }

    override fun onBindViewHolder(holder: ViewBindingHolder, position: Int) {

        if (holder.viewType == VIEW_TYPE_ITEM) {
            val data = dataSource[holder.bindingAdapterPosition]
            onBindViewHolderWithData(holder, data, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val viewBinding = createBinding(parent)
                ViewBindingHolder(viewBinding.root, viewType).apply {
                    itemViewBinding = viewBinding
                }
            }
            VIEW_TYPE_EMPTY -> {
                val emptyViewBinding = createEmptyViewBinding(parent)
                ViewBindingHolder(emptyViewBinding?.root ?: View(parent.context), viewType)
            }
            else -> throw Exception()
        }
    }

    override fun getItemCount(): Int {
        return if (dataSource.isEmpty()) {
            if (hasLoadedData) 1 else 0
        } else dataSource.size
    }

    override fun onViewRecycled(holder: ViewBindingHolder) {
        super.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSource.isEmpty()) {
            if (hasLoadedData) VIEW_TYPE_EMPTY else VIEW_TYPE_PLACEHOLDER
        } else VIEW_TYPE_ITEM
    }

    inner class ViewBindingHolder(itemView: View, val viewType: Int) :
        RecyclerView.ViewHolder(itemView) {
        lateinit var itemViewBinding: TBinding
    }

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_EMPTY = 2
        const val VIEW_TYPE_PLACEHOLDER = 3
    }
}
