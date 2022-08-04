package com.panda.pda.mes.operation.qms.quality_problem_record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.panda.pda.mes.R
import com.panda.pda.mes.base.BaseFragment
import com.panda.pda.mes.common.ModelPropertyCreator
import com.panda.pda.mes.common.adapter.CommonViewBindingAdapter
import com.panda.pda.mes.common.data.CommonApi
import com.panda.pda.mes.common.data.model.FileInfoModel
import com.panda.pda.mes.databinding.FragmentProblemDetialBinding
import com.panda.pda.mes.databinding.ItemProblemRecordDetailFileBinding
import com.panda.pda.mes.databinding.ItemProblemRecordDetailPicBinding
import com.panda.pda.mes.operation.qms.QualityViewModel
import com.panda.pda.mes.operation.qms.data.model.QualityProblemRecordDetailModel

/**
 * created by AnJiwei 2021/10/11
 */
class ProblemDetailFragment: BaseFragment(R.layout.fragment_problem_detial) {

    private val viewBinding by viewBinding<FragmentProblemDetialBinding>()
    private val viewModel by activityViewModels<QualityViewModel>()

    private lateinit var picAdapter: CommonViewBindingAdapter<*, FileInfoModel>
    private lateinit var fileAdapter: CommonViewBindingAdapter<*, FileInfoModel>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topAppBar.setNavigationOnClickListener { navBackListener.invoke(it) }
        val modelProperty = ModelPropertyCreator(
            QualityProblemRecordDetailModel::class.java,
            viewBinding.llPropertyInfo
        )

        picAdapter =
            object : CommonViewBindingAdapter<ItemProblemRecordDetailPicBinding, FileInfoModel>() {
                override fun createBinding(parent: ViewGroup): ItemProblemRecordDetailPicBinding {
                    return ItemProblemRecordDetailPicBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: FileInfoModel,
                    position: Int
                ) {
                    holder.itemViewBinding.ivPicture.load(
                        CommonApi.getFIleUrl(
                        data.fileUrl,
                        data.fileName
                    ))
                    holder.itemViewBinding.root.setOnClickListener { showImageView(data) }
                }
            }

        fileAdapter =
            object : CommonViewBindingAdapter<ItemProblemRecordDetailFileBinding, FileInfoModel>() {
                override fun createBinding(parent: ViewGroup): ItemProblemRecordDetailFileBinding {
                    return ItemProblemRecordDetailFileBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )

                }

                override fun onBindViewHolderWithData(
                    holder: ViewBindingHolder,
                    data: FileInfoModel,
                    position: Int
                ) {
                    holder.itemViewBinding.ivDelete.isVisible = false
                    holder.itemViewBinding.btnFile.text = data.fileName
                    holder.itemViewBinding.root.setOnClickListener {
                        showFileView(data)
                    }
                }

            }

        viewBinding.rvPicList.adapter = picAdapter
        viewBinding.rvFilesList.adapter = fileAdapter
        viewModel.problemRecordDetailData.observe(viewLifecycleOwner) {
            it.reInit()
            modelProperty.setData(it)
            viewBinding.tvRemark.text = it.remark
            picAdapter.refreshData(it.pictureList ?: listOf())
            fileAdapter.refreshData(it.fileList ?: listOf())
        }
    }

    private fun showFileView(data: FileInfoModel) {

    }

    private fun showImageView(data: FileInfoModel) {

    }
}