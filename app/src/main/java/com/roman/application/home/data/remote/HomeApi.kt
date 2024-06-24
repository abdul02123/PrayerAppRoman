package com.roman.application.home.data.remote

import com.roman.application.home.domain.model.response.ResponseData
import retrofit2.Response
import retrofit2.http.GET

interface HomeApi {

    @GET("cities.json")
    suspend fun getCites(): Response<ResponseData>
}