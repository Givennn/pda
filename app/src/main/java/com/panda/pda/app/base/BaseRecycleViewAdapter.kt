package com.panda.pda.app.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * created by AnJiwei 2021/3/15
 */
abstract class BaseRecycleViewAdapter<TBinding: ViewBinding, TSource>(protected val dataSource: MutableList<TSource>):
    RecyclerView.Adapter<BaseRecycleViewAdapter<TBinding, TSource>.ViewHolder>() {

    protected abstract fun createBinding(parent: ViewGroup): TBinding

    protected abstract fun onBindViewHolderWithData(holder:ViewHolder, data: TSource, position: Int)

    fun refreshData(newSource: List<TSource>) {
        dataSource.clear()
        dataSource.addAll(newSource)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataSource[holder.bindingAdapterPosition]
        onBindViewHolderWithData(holder, data, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(createBinding(parent))
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
    }

    inner class ViewHolder(val binding: TBinding): RecyclerView.ViewHolder(binding.root)

}
