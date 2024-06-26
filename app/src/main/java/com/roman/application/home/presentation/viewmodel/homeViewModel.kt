package com.roman.application.home.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.roman.application.home.domain.model.response.city.Cities
import com.roman.application.home.domain.model.response.prayer.CurrentPrayerDetail
import com.roman.application.home.domain.model.response.prayer.Prayers
import com.roman.application.home.domain.model.response.prayer.toPrayersModel
import com.roman.application.home.domain.usecase.GetCitiesUseCase
import com.roman.application.home.domain.usecase.GetPrayerTimeUseCase
import com.roman.application.util.isInternetAvailable
import com.roman.application.util.network.NetworkException
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.network.exceptionHandler
import com.roman.application.util.network.launchApi
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class homeViewModel @Inject constructor(
    private val application: Application,
    private val citiesUseCase: GetCitiesUseCase,
    private val prayerTimeUseCase: GetPrayerTimeUseCase
) : AndroidViewModel(application) {

    var result = MutableLiveData<NetworkResult<Cities>>()
    var noInternetConnection = MutableLiveData<Boolean>()
    var prayerTimeResult = MutableLiveData<NetworkResult<CurrentPrayerDetail>>()
    private val prayerTimesMap = mutableMapOf<String, List<String>>()

    fun getCitiesData() {
        if (application.isInternetAvailable()) {
            result.value = NetworkResult.Loading()
            val exceptionHandler = viewModelScope.exceptionHandler {
                if (it is NetworkException)
                    result.value = NetworkResult.Error(errorResponse = it.errorResponse)
            }

            viewModelScope.launchApi(exceptionHandler) {
                val response = citiesUseCase.runCities()
                result.value = NetworkResult.Success(result = response)
            }
        } else {
            noInternetConnection.value = true
        }
    }


    fun getPrayerTimeData(city: String) {
        if (application.isInternetAvailable()) {
            result.value = NetworkResult.Loading()
            val exceptionHandler = viewModelScope.exceptionHandler {
                if (it is NetworkException)
                    prayerTimeResult.value = NetworkResult.Error(errorResponse = it.errorResponse)
            }

            viewModelScope.launchApi(exceptionHandler) {
                val response = prayerTimeUseCase.runPrayerTimes(city)
                prayerTimesMap.clear()
                prayerTimesMap.putAll(response.prayerTimes)
                val currentDateTime = Date()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date: String = dateFormat.format(currentDateTime)
                val currentPrayerTimesList: List<String> = response.prayerTimes[date] ?: emptyList()
                if (currentPrayerTimesList.isNotEmpty()) {
                    val currentPrayerTimes = currentPrayerTimesList.toPrayersModel(date)
                    val currentPrayer = currentPrayerTimes.getCurrentPrayer()
                    val prayersTime =  ArrayList<Prayers>()
                    prayersTime.add(Prayers("Fajir", currentPrayerTimesList.toPrayersModel(date).fajr.toString()))
                    prayersTime.add(Prayers("Dhuhar", currentPrayerTimesList.toPrayersModel(date).duhur.toString()))
                    prayersTime.add(Prayers("Asr", currentPrayerTimesList.toPrayersModel(date).asr.toString()))
                    prayersTime.add(Prayers("Magrib", currentPrayerTimesList.toPrayersModel(date).maghrib.toString()))
                    prayersTime.add(Prayers("Isha", currentPrayerTimesList.toPrayersModel(date).isha.toString()))
                    val prayerData = CurrentPrayerDetail(
                        name = currentPrayer.first.first,
                        time = currentPrayer.first.second,
                        nextPrayer = currentPrayer.second.first,
                        nextPrayerTime = currentPrayer.second.second,
                        prayersTime = prayersTime
                    )
                    prayerTimeResult.value = NetworkResult.Success(result = prayerData)
                }
            }
        } else {
            noInternetConnection.value = true
        }
    }



    fun messageStatus(status: Boolean){
        noInternetConnection.value = status
    }
}