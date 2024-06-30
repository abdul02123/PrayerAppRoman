package com.roman.application.athkar.data.repository

import com.roman.application.athkar.data.remote.AkhtarApi
import com.roman.application.athkar.domain.model.AkhtarResponse
import com.roman.application.home.domain.model.response.city.Cities
import com.roman.application.util.network.CryptLib
import com.roman.application.util.network.errorResponse
import com.roman.application.util.toResponseModel
import javax.inject.Inject

class AkhtarRepository@Inject constructor(private val akhtarApi: AkhtarApi) {

    suspend fun getAkhtar(): AkhtarResponse {
        val response = akhtarApi.getAkhtar()
        if (response.isSuccessful){
            val decryptedResponse = CryptLib.decryptData(response.body()?.data?: "")
            val getAkhtarResponse: AkhtarResponse = decryptedResponse.toResponseModel()
            return getAkhtarResponse
        }else{
            errorResponse(response)
        }
    }
}