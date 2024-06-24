package com.roman.application.presentation.activity

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityHomeBinding
import com.roman.application.presentation.dialogues.LocationDialogue
import com.roman.application.presentation.viewmodel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseCompatVBActivity<ActivityHomeBinding>() {

    private val viewModel: PhotoViewModel by viewModels()

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun init() {

        mBinding?.imgEdit?.setOnClickListener{
            startActivity(Intent(this@HomeActivity, PrayerTimeActivity::class.java))
        }

//        bindObserver()
        viewModel.getCitiesData()
        showLocationDialogue()
    }


    private fun showLocationDialogue(){
        LocationDialogue.getInstance(message = "", onClick = {

        }).show(supportFragmentManager, null)
    }

    /*private fun bindObserver() {
        viewModel.result.observe(this) {
            mBinding?.progressBar?.makeGone()
            when (it) {
                is NetworkResult.Success -> {
                    setAdapter(it.result as ArrayList<PhotoResponse>)
                }
                is NetworkResult.Error -> {
                    val error = it.errorResponse as ErrorResponse
                    showToast(error.message)
                }
                is NetworkResult.Loading -> {
                mBinding?.progressBar?.makeVisible()
            }
            }
        }

        viewModel.noInternetConnection.observe(this){
            val data = it as Boolean
            if (data){
                viewModel.messageStatus(false)
                showToast(getString(R.string.network_error_message_internet))
            }
        }
    }

    private fun setAdapter(photoArray: ArrayList<PhotoResponse>?) {
        if (photoArray?.isNotEmpty() == true) {
            val groupByAlbum = photoArray.groupBy { it.albumId }
            val sectionAdapter = SectionAdapter(groupByAlbum)
            mBinding?.recyclerView?.adapter = sectionAdapter
        }

    }*/
}