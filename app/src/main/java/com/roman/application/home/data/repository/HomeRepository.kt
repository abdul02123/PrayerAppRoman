package com.roman.application.home.data.repository

import com.roman.application.home.data.remote.HomeApi
import com.roman.application.home.domain.model.response.city.Cities
import com.roman.application.home.domain.model.response.prayer.PrayerTimesResponse
import com.roman.application.util.network.CryptLib
import com.roman.application.util.network.errorResponse
import com.roman.application.util.toResponseModel
import org.json.JSONObject
import javax.inject.Inject

class HomeRepository @Inject constructor(private val homeApi: HomeApi) {

    suspend fun getCites(): Cities {
        val response = homeApi.getCites()
        if (response.isSuccessful){
            val decryptedResponse = CryptLib.decryptData(response.body()?.data?: "")
            val getCitiesResponse: Cities = decryptedResponse.toResponseModel()
            return getCitiesResponse
        }else{
            errorResponse(response)
        }
    }

    suspend fun getPrayerTimes(city: String): PrayerTimesResponse{
        val response = homeApi.getPrayerTime(city)
        if (response.isSuccessful){
            val decryptedResponse = CryptLib.decryptData(response.body()?.data?: "")
            val decryptedResponseObject = JSONObject(decryptedResponse)
            val map = mutableMapOf<String, List<String>>()
            decryptedResponseObject.keys().forEach { key ->
                val value = decryptedResponseObject.getJSONArray(key)
                val list = mutableListOf<String>()
                for (i in 0 until value.length()) {
                    list.add(value.getString(i))
                }
                map[key] = list
            }
            return PrayerTimesResponse(map.toMap())
        }else{
            errorResponse(response)
        }
    }
}