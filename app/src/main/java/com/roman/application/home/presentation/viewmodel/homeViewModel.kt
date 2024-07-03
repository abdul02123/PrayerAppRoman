package com.roman.application.home.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.roman.application.home.domain.model.response.city.Cities
import com.roman.application.home.domain.model.response.prayer.CurrentPrayerDetail
import com.roman.application.home.domain.model.response.prayer.PrayerTimesResponse
import com.roman.application.home.domain.model.response.prayer.Prayers
import com.roman.application.home.domain.model.response.prayer.toPrayersModel
import com.roman.application.home.domain.usecase.GetCitiesUseCase
import com.roman.application.home.domain.usecase.GetPrayerTimeUseCase
import com.roman.application.util.formatDate
import com.roman.application.util.isInternetAvailable
import com.roman.application.util.network.NetworkException
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.network.exceptionHandler
import com.roman.application.util.network.launchApi
import com.roman.application.util.storage.MySharePreference.getSavedData
import com.roman.application.util.storage.MySharePreference.saveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class homeViewModel @Inject constructor(
    private val application: Application,
    private val citiesUseCase: GetCitiesUseCase,
    private val prayerTimeUseCase: GetPrayerTimeUseCase,
) : AndroidViewModel(application) {

    var result = MutableLiveData<NetworkResult<Cities>>()
    var noInternetConnection = MutableLiveData<Boolean>()
    var prayerTimeResult = MutableLiveData<NetworkResult<CurrentPrayerDetail>>()
    var prayerTimeDetail = MutableLiveData<CurrentPrayerDetail>()

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
                    prayerTimeResult.value =
                        NetworkResult.Error(errorResponse = it.errorResponse)
            }

            viewModelScope.launchApi(exceptionHandler) {
               val response = prayerTimeUseCase.runPrayerTimes(city)
                prayerTimeResult.value = NetworkResult.Success(result = response)
            }
        } else {
            noInternetConnection.value = true
        }
    }


    fun showPrayerTimes(prayerName: String) {
        prayerTimeDetail.value = prayerTimeUseCase.showPrayerTimes(prayerName)
    }


    fun messageStatus(status: Boolean) {
        noInternetConnection.value = status
    }
}