package com.roman.application.home.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.roman.application.home.domain.model.response.city.Cities
import com.roman.application.home.domain.usecase.GetCitiesUseCase
import com.roman.application.util.isInternetAvailable
import com.roman.application.util.network.NetworkException
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.network.exceptionHandler
import com.roman.application.util.network.launchApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val application: Application,
    private val photosUseCase: GetCitiesUseCase
) : AndroidViewModel(application) {

    var result = MutableLiveData<NetworkResult<Cities>>()
    var noInternetConnection = MutableLiveData<Boolean>()

    fun getCitiesData() {
        if (application.isInternetAvailable()) {
            result.value = NetworkResult.Loading()
            val exceptionHandler = viewModelScope.exceptionHandler {
                if (it is NetworkException)
                    result.value = NetworkResult.Error(errorResponse = it.errorResponse)
            }

            viewModelScope.launchApi(exceptionHandler) {
                val response = photosUseCase.runCities()
                result.value = NetworkResult.Success(result = response)
            }
        } else {
            noInternetConnection.value = true
        }
    }



    fun messageStatus(status: Boolean){
        noInternetConnection.value = status
    }
}