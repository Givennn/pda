package com.panda.pda.app.operation.fms.mission

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.panda.pda.app.R
import com.panda.pda.app.common.data.model.FileInfoModel
import com.panda.pda.app.databinding.ItemTaskReportPhotoBinding

/**
 * created by AnJiwei 2021/9/3
 */
class TaskReportInputPhotoAdapter :
    RecyclerView.Adapter<TaskReportInputPhotoAdapter.PhotoHolder>() {
    private val dataSource = mutableListOf<FileInfoModel>()

    override fun getItemCount(): Int {
        return if (dataSource.size >= 3) 3 else dataSource.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == dataSource.size)
            VIEW_TYPE_ACTION
        else VIEW_TYPE_PHOTO
    }

    fun setData(photos: List<FileInfoModel>) {
        dataSource.clear()
        dataSource.addAll(photos)
        notifyDataSetChanged()
    }

    inner class PhotoHolder(itemView: View, val viewType: Int) :
        RecyclerView.ViewHolder(itemView) {
        lateinit var itemViewBinding: ItemTaskReportPhotoBinding
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return if (viewType == VIEW_TYPE_ACTION) {
            PhotoHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task_report_take_photo, parent, false), viewType)
        } else {
            val viewBinding =
                ItemTaskReportPhotoBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
            PhotoHolder(viewBinding.root, viewType).apply { itemViewBinding = viewBinding }

        }
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        if (holder.viewType == VIEW_TYPE_ACTION) {
            holder.itemView.setOnClickListener { onTakePhotoAction.invoke() }
        } else {
            val data = dataSource[holder.bindingAdapterPosition]
            holder.itemViewBinding.apply {
                ivReportPhoto.load(data.fileLocalUri)
                ivDelete.setOnClickListener {
                    dataSource.remove(data)
                    notifyItemRemoved(holder.bindingAdapterPosition)
                }
            }

        }
    }

    fun getDataSource(): MutableList<FileInfoModel> {
        return dataSource
    }

    fun addPhoto(uri: Uri) {
        dataSource.add(FileInfoModel("test", uri.toString(), ""))
    }

    companion object {
        const val VIEW_TYPE_ACTION = 1
        const val VIEW_TYPE_PHOTO = 2
    }

    var onTakePhotoAction: () -> Unit = { }
}