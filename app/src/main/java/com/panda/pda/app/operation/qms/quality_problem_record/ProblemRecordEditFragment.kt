package com.panda.pda.app.operation.qms.quality_problem_record

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.panda.pda.app.R
import com.panda.pda.app.base.BaseFragment
import com.panda.pda.app.databinding.FragmentProblemRecordEditBinding
import com.panda.pda.app.operation.qms.data.model.QualityProblemRecordDetailModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

/**
 * created by AnJiwei 2021/10/12
 */
class ProblemRecordEditFragment: BaseFragment(R.layout.fragment_problem_record_edit) {

    private var detailModel: QualityProblemRecordDetailModel? = null

    private val viewBinding by viewBinding<FragmentProblemRecordEditBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailStr = arguments?.getString(DETAIL_KEY)
        if (detailStr != null) {
            detailModel = getProblemRecordJsonAdapter().fromJson(detailStr)
        }
    }

    private fun setDetailModel(model: QualityProblemRecordDetailModel) {
        viewBinding.apply {

        }
    }

//    private fun buildDetailModel(): QualityProblemRecordDetailModel {
//        val result = QualityProblemRecordDetailModel
//    }
    companion object {
        const val DETAIL_KEY = "detail_key"

        fun getProblemRecordJsonAdapter(): JsonAdapter<QualityProblemRecordDetailModel> {
            return Moshi.Builder().build().adapter(QualityProblemRecordDetailModel::class.java)
        }
    }
}