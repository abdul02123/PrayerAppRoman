package com.roman.application.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.roman.application.R
import com.roman.application.domain.model.photo.response.PhotoResponse
import com.roman.application.domain.usecase.GetPhotosUseCase
import com.roman.application.util.isInternetAvailable
import com.roman.application.util.network.NetworkException
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.network.exceptionHandler
import com.roman.application.util.network.launchApi
import com.roman.application.util.storage.MySharePreference.getSavedData
import com.roman.application.util.storage.MySharePreference.saveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val application: Application,
    private val photosUseCase: GetPhotosUseCase
) : AndroidViewModel(application) {

    var result = MutableLiveData<NetworkResult<PhotoResponse>>()
    var noInternetConnection = MutableLiveData<Boolean>()

    init {
        getPhotoData()
    }

    private fun getPhotoData() {
        if (application.isInternetAvailable()) {
            result.value = NetworkResult.Loading()
            val exceptionHandler = viewModelScope.exceptionHandler {
                if (it is NetworkException)
                    result.value = NetworkResult.Error(errorResponse = it.errorResponse)
            }

            viewModelScope.launchApi(exceptionHandler) {
                val response = photosUseCase.run()
                saveData(response)
                result.value = NetworkResult.Success(result = response)
            }
        } else {
            noInternetConnection.value = true
            result.value = NetworkResult.Success(result = getOfflineResponse())
        }
    }


    private fun getOfflineResponse(): ArrayList<PhotoResponse>? {
        try {
            if (!getSavedData().isNullOrEmpty()) {
                val gson = Gson()
                val response = object : TypeToken<ArrayList<PhotoResponse>>() {}.type
                return gson.fromJson(getSavedData(), response)
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        return ArrayList()
    }

    fun messageStatus(status: Boolean){
        noInternetConnection.value = status
    }
}