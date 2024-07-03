package com.roman.application.home.presentation.activity

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.roman.application.R
import com.roman.application.athkar.presentation.activity.AthkarActivity
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.base.LocationManager
import com.roman.application.databinding.ActivityHomeBinding
import com.roman.application.home.domain.model.response.city.City
import com.roman.application.home.domain.model.response.prayer.CurrentPrayerDetail
import com.roman.application.home.domain.model.response.prayer.Prayers
import com.roman.application.home.presentation.dialogues.BottomSheetDialogue
import com.roman.application.home.presentation.dialogues.LocationDialogue
import com.roman.application.home.presentation.viewmodel.homeViewModel
import com.roman.application.prayer.presentation.activities.PrayerTimeActivity
import com.roman.application.util.PermissionManager.Companion.requiredLocationPermissions
import com.roman.application.util.SelectionType
import com.roman.application.util.network.ErrorResponse
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.showToast
import com.roman.application.util.storage.MySharePreference.getCity
import com.roman.application.util.storage.MySharePreference.getSavedData
import com.roman.application.util.storage.MySharePreference.saveData
import com.roman.application.util.storage.MySharePreference.setCity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class HomeActivity : BaseCompatVBActivity<ActivityHomeBinding>() {

    private val viewModel: homeViewModel by viewModels()
    private var dialogueLocation: LocationDialogue? = null
    private var prayerTimes: ArrayList<Prayers>? = null
    private var cities: ArrayList<City>? = null
    private var locationManager: LocationManager?= null

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun init() {

        mBinding?.apply {
            imgEdit.setOnClickListener {
                startActivity(
                    Intent(this@HomeActivity, PrayerTimeActivity::class.java)
                        .putExtra("prayerTimes", prayerTimes)
                )
            }

            lyAkhtar.setOnClickListener {
                startActivity(
                    Intent(this@HomeActivity, AthkarActivity::class.java))
            }

             lyFajir.setOnClickListener {
                viewModel.showPrayerTimes(tvFajir.text.toString())
            }

            lyDuhur.setOnClickListener {
                viewModel.showPrayerTimes(tvDuhur.text.toString())
            }

            lyAsr.setOnClickListener {
                viewModel.showPrayerTimes(tvAsr.text.toString())
            }
            lyMaghreb.setOnClickListener {
                viewModel.showPrayerTimes(tvMagrib.text.toString())
            }

            lyIsha.setOnClickListener {
                viewModel.showPrayerTimes(tvIsha.text.toString())
            }
        }

        bindObserver()
        if (getCity().isNullOrEmpty()) {
            showProgressDialogue()
            viewModel.getCitiesData()
        } else {
            prayerTimeData()
        }
    }

    private fun prayerTimeData(){
        mBinding?.tvLocation?.text = getCity()
        showProgressDialogue()
        viewModel.getPrayerTimeData(getCity() ?: "")
    }

    private fun showLocationDialogue(citiesList: ArrayList<City>) {
        dialogueLocation =
            LocationDialogue.getInstance(message = "", onClick = { selectionType, city ->
                when (selectionType) {
                    SelectionType.CITY.indentifier -> {
                        showCitiesDialogue(citiesList)
                    }
                    SelectionType.AUTO.indentifier -> {
                        initLocation()
                        activityResultLauncher.launch(requiredLocationPermissions)
                    }

                    SelectionType.DONE.indentifier -> {
                        mBinding?.tvLocation?.text = city?.nameEn
                        setCity(city?.file ?: "")
                        showProgressDialogue()
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
            when (it) {
                is NetworkResult.Success -> {
                    cities = it.result?.cities
                    showLocationDialogue(it.result?.cities ?: ArrayList())
                    hideProgressDialogue()
                }

                is NetworkResult.Error -> {
                    val error = it.errorResponse as ErrorResponse
                    showToast(error.message)
                    hideProgressDialogue()
                }

                is NetworkResult.Loading -> {
                }
            }
        }
        viewModel.prayerTimeResult.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    showBannerData(it.result as CurrentPrayerDetail, true)
                    prayerTimes(it.result)
                    hideProgressDialogue()
                }

                is NetworkResult.Error -> {
                    hideProgressDialogue()

                }

                is NetworkResult.Loading -> {

                }
            }

            viewModel.prayerTimeDetail.observe(this){
                val data = it as CurrentPrayerDetail
                showBannerData(data)
            }
        }
    }


    private fun showBannerData(prayer: CurrentPrayerDetail, isShowCurrentSelectedPrayer: Boolean = false) {
        mBinding?.apply {
            tvPrayerName.text = prayer.name
            tvNamazTime.text = prayer.time?.substring(0, prayer.time.length - 3)?.trim()
            tvTime.text = prayer.time?.substring(prayer.time.length - 2)?.uppercase(Locale.ROOT)
            tvNextPrayerTitle.text = "Next Pray: ${prayer.nextPrayer}"
            tvNextPrayerTime.text = prayer.nextPrayerTime
            if (isShowCurrentSelectedPrayer){
                mBinding?.apply {
                    when (prayer.name) {
                        "Fajir" -> {
                            frameFajir.setBackgroundResource(R.drawable.bg_round_stroke_2)
                        }

                        "Dhuhar" -> {
                            frameDuhur.setBackgroundResource(R.drawable.bg_round_stroke_2)
                        }

                        "Asr" -> {
                            frameAsr.setBackgroundResource(R.drawable.bg_round_stroke_2)
                        }

                        "Magrib" -> {
                            frameMagrib.setBackgroundResource(R.drawable.bg_round_stroke_2)
                        }

                        "Isha" -> {
                            frameIsha.setBackgroundResource(R.drawable.bg_round_stroke_2)
                        }

                    }
                }
            }

        }


    }

    private fun prayerTimes(prayer: CurrentPrayerDetail){
        prayerTimes = prayer.prayersTime
        val savedPrayersStatus = getSavedData()

        for (time in prayerTimes?: ArrayList()){
            for (savedTime in savedPrayersStatus){
                if (time.namazName == savedTime.namazName){
                    time.isAlarmOn = savedTime.isAlarmOn
                }
            }
        }
        saveData(prayerTimes?: ArrayList())
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.contains(true)) {
                locationManager?.checkLocationEnable()
            }
        }

    private fun initLocation() {
        locationManager = LocationManager()
        locationManager?.setActivity(this)
        locationManager?.setLocationListener(listener = { locationResult ->

            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(locationResult.lastLocation?.latitude?:0.0, locationResult.lastLocation?.longitude?:0.0, 1)

            if (!addresses.isNullOrEmpty()){
                val city = addresses[0].locality
                setCity(cities?.find { it.file.equals(city, true) }?.file?: "amman")
                dialogueLocation?.dismiss()
                prayerTimeData()
                locationManager?.removeLocationListener()
            }
        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        locationManager?.onActivityResult(requestCode, resultCode, data)
    }
}


