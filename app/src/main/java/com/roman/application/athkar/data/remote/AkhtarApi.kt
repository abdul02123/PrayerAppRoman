package com.roman.application.athkar.data.remote

import com.roman.application.base.core.ResponseData
import retrofit2.Response
import retrofit2.http.GET

interface AkhtarApi {
    @GET("athkars.json")
    suspend fun getAkhtar(): Response<ResponseData>
}