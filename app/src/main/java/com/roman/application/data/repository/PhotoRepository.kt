package com.roman.application.data.repository

import com.roman.application.data.remote.PhotoApi
import com.roman.application.domain.model.response.city.Cities
import com.roman.application.util.network.CryptLib
import com.roman.application.util.network.errorResponse
import com.roman.application.util.toResponseModel
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val photoApi: PhotoApi) {
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