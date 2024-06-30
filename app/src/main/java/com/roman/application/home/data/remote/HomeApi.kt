package com.roman.application.home.data.remote

import com.roman.application.base.core.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApi {

    @GET("cities.json")
    suspend fun getCites(): Response<ResponseData>

    @GET("prayers/{city}.json")
    suspend fun getPrayerTime(@Path("city") city: String): Response<ResponseData>
}