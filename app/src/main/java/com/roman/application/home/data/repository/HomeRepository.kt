package com.roman.application.home.data.repository

import com.roman.application.home.data.remote.HomeApi
import com.roman.application.home.domain.model.response.city.Cities
import com.roman.application.util.network.CryptLib
import com.roman.application.util.network.errorResponse
import com.roman.application.util.toResponseModel
import javax.inject.Inject

class HomeRepository @Inject constructor(private val photoApi: HomeApi) {

    suspend fun getCites(): Cities {
        val response = photoApi.getCites()
        if (response.isSuccessful){
            val decryptedResponse = CryptLib.decryptData(response.body()?.data?: "")
            val getCitiesResponse: Cities = decryptedResponse.toResponseModel()
            return getCitiesResponse
        }else{
            errorResponse(response)
        }
    }
}