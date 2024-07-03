package com.roman.application.home.domain.usecase

import com.roman.application.home.data.repository.HomeRepository
import com.roman.application.home.domain.model.response.prayer.CurrentPrayerDetail
import com.roman.application.home.domain.model.response.prayer.PrayerTimesResponse
import com.roman.application.home.domain.model.response.prayer.Prayers
import com.roman.application.home.domain.model.response.prayer.toPrayersModel
import com.roman.application.util.enums.DateFormat
import com.roman.application.util.enums.PrayerName
import com.roman.application.util.formatDate
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.storage.MySharePreference.getSavedData
import com.roman.application.util.storage.MySharePreference.saveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetPrayerTimeUseCase @Inject constructor(private val photoRepository: HomeRepository) {

    private val prayerTimesMap = mutableMapOf<String, List<String>>()
    private var response: PrayerTimesResponse? = null
    private val prayersTime = ArrayList<Prayers>()
    private var prayerData: CurrentPrayerDetail?= null


    suspend fun runPrayerTimes(city: String): CurrentPrayerDetail? {
         withContext(Dispatchers.IO) {
             response = photoRepository.getPrayerTimes(city)
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
                         PrayerName.FAJIR.identifierName,
                         currentPrayerTimesList.toPrayersModel(date).fajr.toString()
                     )
                 )
                 prayersTime.add(
                     Prayers(
                         PrayerName.Dhuhar.identifierName,
                         currentPrayerTimesList.toPrayersModel(date).duhur.toString()
                     )
                 )
                 prayersTime.add(
                     Prayers(
                         PrayerName.ASR.identifierName,
                         currentPrayerTimesList.toPrayersModel(date).asr.toString()
                     )
                 )
                 prayersTime.add(
                     Prayers(
                         PrayerName.MAGRIB.identifierName,
                         currentPrayerTimesList.toPrayersModel(date).maghrib.toString()
                     )
                 )
                 prayersTime.add(
                     Prayers(
                         PrayerName.ISHA.identifierName,
                         currentPrayerTimesList.toPrayersModel(date).isha.toString()
                     )
                 )
                  prayerData = CurrentPrayerDetail(
                     name = currentPrayer.first.first,
                     time = currentPrayer.first.second,
                     nextPrayer = currentPrayer.second.first,
                     nextPrayerTime = currentPrayer.second.second,
                     prayersTime = prayersTime
                 )
                 if (getSavedData().isEmpty()) {
                     saveData(prayersTime)
                 }

             }
        }
        return prayerData
    }

    fun showPrayerTimes(prayerName: String): CurrentPrayerDetail {
        when (prayerName) {
            PrayerName.FAJIR.identifierName -> {
                    return getPrayerDetails(0)
            }
            PrayerName.Dhuhar.identifierName -> {
                return getPrayerDetails(1)
            }

            PrayerName.ASR.identifierName -> {
                return getPrayerDetails(2)
            }

            PrayerName.MAGRIB.identifierName -> {
                return getPrayerDetails(3)
            }

            PrayerName.ISHA.identifierName -> {
                return getPrayerDetails(4, true)
            }

        }
        return getPrayerDetails(0)
    }

    private fun getPrayerDetails(index: Int, isIshaPrayer: Boolean = false): CurrentPrayerDetail {
        if (isIshaPrayer) {
            val prayerData = CurrentPrayerDetail(
                name = prayersTime[index].namazName,
                time = prayersTime[index].namazTime.formatDate(
                    DateFormat.COMPLETE_FORMAT.identifierName,
                    DateFormat.HH_MM_A.identifierName
                ),
                nextPrayer = "Fajir",
                nextPrayerTime = getFinalNextFajirPrayer()
            )
            return prayerData

        } else {
            val prayerData = CurrentPrayerDetail(
                name = prayersTime[index].namazName,
                time = prayersTime[index].namazTime.formatDate(
                    DateFormat.COMPLETE_FORMAT.identifierName,
                    DateFormat.HH_MM_A.identifierName
                ),
                nextPrayer = prayersTime[index + 1].namazName,
                nextPrayerTime = prayersTime[index + 1].namazTime.formatDate(
                    DateFormat.COMPLETE_FORMAT.identifierName,
                    DateFormat.HH_MM_A.identifierName
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
            .formatDate(DateFormat.COMPLETE_FORMAT.identifierName, DateFormat.HH_MM_A.identifierName)

    }
}