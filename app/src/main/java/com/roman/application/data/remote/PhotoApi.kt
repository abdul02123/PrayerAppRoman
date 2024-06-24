package com.roman.application.data.remote

import com.roman.application.domain.model.response.ResponseData
import retrofit2.Response
import retrofit2.http.GET

interface PhotoApi {

    @GET("cities.json")
    suspend fun getCites(): Response<ResponseData>
}