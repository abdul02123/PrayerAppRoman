package com.roman.application.athkar.presentation

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.roman.application.athkar.AkhtarAdapter
import com.roman.application.athkar.domain.model.AkhtarResponse
import com.roman.application.athkar.domain.model.Athkar
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityAthkarBinding
import com.roman.application.home.domain.model.response.prayer.CurrentPrayerDetail
import com.roman.application.util.network.ErrorResponse
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AthkarActivity : BaseCompatVBActivity<ActivityAthkarBinding>() {

    private val viewModel: AkhtarViewModel by viewModels()

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityAthkarBinding {
        return ActivityAthkarBinding.inflate(layoutInflater)
    }

    override fun init() {

        bindObserver()
        mBinding?.appCompatImageView2?.setOnClickListener {
            finish()
        }

        viewModel.getAkhtarData()
    }


    private fun setAdapter(list: ArrayList<Athkar?>?){
        val adapter = AkhtarAdapter(list)
        mBinding?.recyclerView?.adapter = adapter
    }


    private fun bindObserver() {
        viewModel.result.observe(this) {
//            mBinding?.progressBar?.makeGone()
            when (it) {
                is NetworkResult.Success -> {
                    val data = it as AkhtarResponse
                    setAdapter(data.athkars)
                }

                is NetworkResult.Error -> {
                    val error = it.errorResponse as ErrorResponse
                    showToast(error.message)
                }

                is NetworkResult.Loading -> {
//                mBinding?.progressBar?.makeVisible()
                }
            }
        }
    }
}