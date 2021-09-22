package com.panda.pda.app.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * created by AnJiwei 2021/9/22
 */
class EmptyAdapter(@LayoutRes val layoutRes: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isEmpty = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return EmptyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return if (isEmpty) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return Item_VIEW_TYPE
    }

    inner class EmptyViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

    companion object {
        const val Item_VIEW_TYPE = 2000
    }
}