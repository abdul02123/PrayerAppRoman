package com.roman.application.home.presentation.activity

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityHomeBinding
import com.roman.application.home.domain.model.response.city.City
import com.roman.application.home.presentation.dialogues.BottomSheetDialogue
import com.roman.application.home.presentation.dialogues.LocationDialogue
import com.roman.application.home.presentation.viewmodel.PhotoViewModel
import com.roman.application.util.SelectionType
import com.roman.application.util.network.ErrorResponse
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseCompatVBActivity<ActivityHomeBinding>() {

    private val viewModel: PhotoViewModel by viewModels()
    private var dialogueLocation: LocationDialogue? = null

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun init() {

        mBinding?.imgEdit?.setOnClickListener {
            startActivity(Intent(this@HomeActivity, PrayerTimeActivity::class.java))
        }
        bindObserver()
        viewModel.getCitiesData()
    }


    private fun showLocationDialogue(citiesList: ArrayList<City>) {
        dialogueLocation =
            LocationDialogue.getInstance(message = "", onClick = { selectionType, city ->
                when (selectionType) {
                    SelectionType.CITY.indentifier -> {
                        showCitiesDialogue(citiesList)
                    }

                    SelectionType.DONE.indentifier -> {

                    }
                }

            })
        dialogueLocation?.show(supportFragmentManager, null)
    }


    private fun showCitiesDialogue(citiesList: ArrayList<City>) {
        BottomSheetDialogue.getInstance(title = "", citiesList = citiesList, onClick = {
            dialogueLocation?.setLocationName(it)

        }).show(supportFragmentManager, null)
    }

    private fun bindObserver() {
        viewModel.result.observe(this) {
//            mBinding?.progressBar?.makeGone()
            when (it) {
                is NetworkResult.Success -> {
                    showLocationDialogue(it.result?.cities ?: ArrayList())
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

        /* viewModel.noInternetConnection.observe(this){
             val data = it as Boolean
             if (data){
                 viewModel.messageStatus(false)
                 showToast(getString(R.string.network_error_message_internet))
             }*/
    }
}

/*private fun setAdapter(photoArray: ArrayList<PhotoResponse>?) {
    if (photoArray?.isNotEmpty() == true) {
        val groupByAlbum = photoArray.groupBy { it.albumId }
        val sectionAdapter = SectionAdapter(groupByAlbum)
        mBinding?.recyclerView?.adapter = sectionAdapter
    }

}*/
