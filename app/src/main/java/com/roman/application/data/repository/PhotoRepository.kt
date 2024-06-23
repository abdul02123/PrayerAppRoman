package com.roman.application.data.repository

import com.roman.application.data.remote.PhotoApi
import com.roman.application.domain.model.photo.response.PhotoResponse
import com.roman.application.util.network.errorResponse
import okhttp3.ResponseBody
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val photoApi: PhotoApi) {

    suspend fun getAllPhotos(): ArrayList<PhotoResponse>{
        val response = photoApi.getPhotos()
        return if (response.isSuccessful){
            requireNotNull(response.body())
        } else{
            errorResponse(response)
        }
    }
    
    suspend fun getCites(): ResponseBody {
        val response = photoApi.getCites()
        return response
    }
}