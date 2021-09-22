package com.panda.pda.app.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.panda.pda.app.R

/**
 * created by AnJiwei 2021/9/22
 */
class HeaderAdapter(private val moduleName: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_operation_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tvHeader = holder.itemView.findViewById<TextView>(R.id.tv_header)
        tvHeader.text = moduleName
    }

    override fun getItemCount(): Int {
        return 1
    }
//
//    override fun getItemViewType(position: Int): Int {
//        return ITEM_VIEW_TYPE
//    }

    inner class HeaderViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

//    companion object {
//        const val ITEM_VIEW_TYPE = 2000
//    }
}