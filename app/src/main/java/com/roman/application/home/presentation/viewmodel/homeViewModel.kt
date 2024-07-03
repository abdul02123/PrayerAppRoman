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
    private val prayerTimesMap = mutableMapOf<String, List<String>>()
    private var response: PrayerTimesResponse? = null
    private val prayersTime = ArrayList<Prayers>()

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
                response = prayerTimeUseCase.runPrayerTimes(city)
                prayerTimesMap.clear()
                response?.prayerTimes?.let { it1 -> prayerTimesMap.putAll(it1) }
                val currentDateTime = Date()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date: String = dateFormat.format(currentDateTime)
                val currentPrayerTimesList: List<String> =
                    response?.prayerTimes?.get(date) ?: emptyList()
                if (currentPrayerTimesList.isNotEmpty()) {
                    val currentPrayerTimes = currentPrayerTimesList.toPrayersModel(date)
                    val currentPrayer = currentPrayerTimes.getCurrentPrayer()
                    prayersTime.add(
                        Prayers(
                            "Fajir",
                            currentPrayerTimesList.toPrayersModel(date).fajr.toString()
                        )
                    )
                    prayersTime.add(
                        Prayers(
                            "Dhuhar",
                            currentPrayerTimesList.toPrayersModel(date).duhur.toString()
                        )
                    )
                    prayersTime.add(
                        Prayers(
                            "Asr",
                            currentPrayerTimesList.toPrayersModel(date).asr.toString()
                        )
                    )
                    prayersTime.add(
                        Prayers(
                            "Magrib",
                            currentPrayerTimesList.toPrayersModel(date).maghrib.toString()
                        )
                    )
                    prayersTime.add(
                        Prayers(
                            "Isha",
                            currentPrayerTimesList.toPrayersModel(date).isha.toString()
                        )
                    )
                    val prayerData = CurrentPrayerDetail(
                        name = currentPrayer.first.first,
                        time = currentPrayer.first.second,
                        nextPrayer = currentPrayer.second.first,
                        nextPrayerTime = currentPrayer.second.second,
                        prayersTime = prayersTime
                    )
                    if (getSavedData().isEmpty()) {
                        saveData(prayersTime)
                    }
                    prayerTimeResult.value = NetworkResult.Success(result = prayerData)
                }
            }
        } else {
            noInternetConnection.value = true
        }
    }


    fun showPrayerTimes(prayerName: String) {

        when (prayerName) {
            "Fajir" -> {
                prayerTimeDetail.value = getPrayerDetails(0)

            }

            "Dhuhar" -> {
                prayerTimeDetail.value = getPrayerDetails(1)
            }

            "Asr" -> {
                prayerTimeDetail.value = getPrayerDetails(2)
            }

            "Magrib" -> {
                prayerTimeDetail.value = getPrayerDetails(3)
            }

            "Isha" -> {
                prayerTimeDetail.value = getPrayerDetails(4, true)
            }

        }

    }


    private fun getPrayerDetails(index: Int, isIshaPrayer: Boolean = false): CurrentPrayerDetail {
        if (isIshaPrayer) {
            val prayerData = CurrentPrayerDetail(
                name = prayersTime[index].namazName,
                time = prayersTime[index].namazTime.formatDate(
                    "EE MMM dd HH:mm:ss 'GMT'Z yyyy",
                    "hh:mm a"
                ),
                nextPrayer = "Fajir",
                nextPrayerTime = getFinalNextFajirPrayer()
            )
            return prayerData

        } else {
            val prayerData = CurrentPrayerDetail(
                name = prayersTime[index].namazName,
                time = prayersTime[index].namazTime.formatDate(
                    "EE MMM dd HH:mm:ss 'GMT'Z yyyy",
                    "hh:mm a"
                ),
                nextPrayer = prayersTime[index + 1].namazName,
                nextPrayerTime = prayersTime[index + 1].namazTime.formatDate(
                    "EE MMM dd HH:mm:ss 'GMT'Z yyyy",
                    "hh:mm a"
                )
            )
            return prayerData
        }

    }


    private fun getNextDayDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar
    }


    private fun getFinalNextFajirPrayer(): String {
        val nextDayDate = getNextDayDate()
        val year = nextDayDate.get(Calendar.YEAR)
        val month = nextDayDate.get(Calendar.MONTH) + 1
        val day = nextDayDate.get(Calendar.DAY_OF_MONTH)
        var dayOfMonth = "$month"
        var monthOfYear = "$day"
        if (day in 0..9) {
            dayOfMonth = "0$day"
        }
        if (month in 0..9) {
            monthOfYear = "0$month"
        }
        val date = "$dayOfMonth/$monthOfYear/$year"
        val currentPrayerTimesList: List<String> = response?.prayerTimes?.get(date) ?: emptyList()
        return currentPrayerTimesList.toPrayersModel(date).fajr.toString()
            .formatDate("EE MMM dd HH:mm:ss 'GMT'Z yyyy", "hh:mm a")

    }


    fun messageStatus(status: Boolean) {
        noInternetConnection.value = status
    }
}