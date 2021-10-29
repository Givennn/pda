package com.panda.pda.app.operation.qms.quality_problem_record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.api.load
import com.google.android.material.tabs.TabLayoutMediator
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.common.ModelPropertyCreator
import com.panda.pda.app.common.adapter.CommonViewBindingAdapter
import com.panda.pda.app.common.data.CommonApi
import com.panda.pda.app.common.data.model.FileInfoModel
import com.panda.pda.app.databinding.FragmentProblemDetialBinding
import com.panda.pda.app.databinding.FragmentQualityDetailBinding
import com.panda.pda.app.databinding.ItemProblemRecordDetailFileBinding
import com.panda.pda.app.databinding.ItemProblemRecordDetailPicBinding
import com.panda.pda.app.operation.qms.QualityViewModel
import com.panda.pda.app.operation.qms.data.model.QualityProblemRecordDetailModel

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
                    holder.itemViewBinding.btnFile.text = data.fileName
                    holder.itemViewBinding.root.setOnClickListener {
                        showFileView(data)
                    }
                }

            }

        viewBinding.rvPicList.adapter = picAdapter
        viewBinding.rvFilesList.adapter = fileAdapter
        viewModel.problemRecordDetailData.observe(viewLifecycleOwner, {
            modelProperty.setData(it)
            viewBinding.tvRemark.text = it.remark
            picAdapter.refreshData(it.pictureList?: listOf())
            fileAdapter.refreshData(it.fileList?: listOf())
        })
    }

    private fun showFileView(data: FileInfoModel) {

    }

    private fun showImageView(data: FileInfoModel) {

    }
//        viewBinding.apply {
//            viewPage.adapter = object : FragmentStateAdapter(parentFragmentManager, lifecycle) {
//                override fun getItemCount(): Int {
//                    return 2
//                }
//
//                override fun createFragment(position: Int): Fragment {
//                    return when (position) {
//                        0 -> ProblemDetailInfoFragment()
//                        1 -> ProblemDetailRecordFragment()
//                        else -> throw IndexOutOfBoundsException()
//                    }
//                }
//
//            }
//            TabLayoutMediator(tabLayout, viewPage) { tab, position ->
//                tab.text = when (position) {
//                    0 -> getString(R.string.basic_info)
//                    1 -> getString(R.string.operate_record)
//                    else -> ""
//                }
//            }.attach()
//        }
//    }
}