package com.roman.application.data.remote

import com.roman.application.domain.model.photo.response.PhotoResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface PhotoApi {

    @GET("photos")
    suspend fun getPhotos() : Response<ArrayList<PhotoResponse>>

    @GET("cities.json")
    suspend fun getCites(): ResponseBody
}