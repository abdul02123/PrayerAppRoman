package com.roman.application.home.presentation.activity

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityHomeBinding
import com.roman.application.home.domain.model.response.city.City
import com.roman.application.home.domain.model.response.prayer.CurrentPrayerDetail
import com.roman.application.home.presentation.dialogues.BottomSheetDialogue
import com.roman.application.home.presentation.dialogues.LocationDialogue
import com.roman.application.home.presentation.viewmodel.homeViewModel
import com.roman.application.prayer.presentation.activities.PrayerTimeActivity
import com.roman.application.util.SelectionType
import com.roman.application.util.network.ErrorResponse
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class HomeActivity : BaseCompatVBActivity<ActivityHomeBinding>() {

    private val viewModel: homeViewModel by viewModels()
    private var dialogueLocation: LocationDialogue? = null
    private var prayerTimes: ArrayList<String>?= null

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun init() {

        mBinding?.imgEdit?.setOnClickListener {
            startActivity(Intent(this@HomeActivity, PrayerTimeActivity::class.java)
                .putExtra("prayerTimes",  prayerTimes))
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
                        mBinding?.tvLocation?.text = city?.nameEn
                        viewModel.getPrayerTimeData(city?.file ?: "")
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
        viewModel.prayerTimeResult.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    showBannerData(it.result as CurrentPrayerDetail)
                }

                is NetworkResult.Error -> {

                }

                is NetworkResult.Loading -> {

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


    private fun showBannerData(prayer: CurrentPrayerDetail) {
        mBinding?.apply {
            tvPrayerName.text = prayer.name
            tvNamazTime.text = prayer.time.substring(0, prayer.time.length - 3).trim()
            tvTime.text = prayer.time.substring(prayer.time.length - 2).uppercase(Locale.ROOT)
            tvNextPrayerTitle.text = "Next Pray: ${prayer.nextPrayer}"
            tvNextPrayerTime.text = prayer.nextPrayerTime

        }
        prayerTimes = prayer.prayersTime.toCollection(ArrayList())
        if (!prayerTimes.isNullOrEmpty()){
            prayerTimes!!.removeAt(1)
        }
    }


    /*private fun setAdapter(photoArray: List<String>?) {
        if (photoArray?.isNotEmpty() == true) {
            val groupByAlbum = photoArray.groupBy { it.albumId }
            val sectionAdapter = SectionAdapter(groupByAlbum)
            mBinding?.recyclerView?.adapter = sectionAdapter
        }
    }*/
}


