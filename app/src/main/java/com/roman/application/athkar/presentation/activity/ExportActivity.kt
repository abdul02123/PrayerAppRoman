package com.roman.application.athkar.presentation.activity

import android.view.LayoutInflater
import android.view.View
import com.roman.application.athkar.domain.model.Athkar
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityExportBinding

class ExportActivity :  BaseCompatVBActivity<ActivityExportBinding>() {

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityExportBinding {
        return ActivityExportBinding.inflate(layoutInflater)
    }

    override fun init() {
        if (intent.getSerializableExtra("export") != null){
            val data = intent.getSerializableExtra("export") as String
            val isVideo = intent.getSerializableExtra("isVideo") as Boolean
            mBinding?.tvText?.text = data

            if (isVideo){
                mBinding?.imgMedia?.visibility = View.VISIBLE
                mBinding?.tvPreview?.visibility = View.VISIBLE
            }else{
                mBinding?.imgMedia?.visibility = View.GONE
                mBinding?.tvPreview?.visibility = View.GONE
            }
        }

        mBinding?.appCompatImageView2?.setOnClickListener {
            finish()
        }
    }
}