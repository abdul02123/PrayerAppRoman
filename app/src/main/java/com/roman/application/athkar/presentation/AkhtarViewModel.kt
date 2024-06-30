package com.roman.application.athkar.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.roman.application.athkar.domain.model.AkhtarResponse
import com.roman.application.athkar.domain.usecase.GetAkhtarUseCase
import com.roman.application.home.domain.model.response.city.Cities
import com.roman.application.util.isInternetAvailable
import com.roman.application.util.network.NetworkException
import com.roman.application.util.network.NetworkResult
import com.roman.application.util.network.exceptionHandler
import com.roman.application.util.network.launchApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AkhtarViewModel @Inject constructor(
    private val application: Application,
    private val akhtarUseCase: GetAkhtarUseCase
) : AndroidViewModel(application) {

    var result = MutableLiveData<NetworkResult<AkhtarResponse>>()
    var noInternetConnection = MutableLiveData<Boolean>()

    fun getAkhtarData() {
        if (application.isInternetAvailable()) {
            result.value = NetworkResult.Loading()
            val exceptionHandler = viewModelScope.exceptionHandler {
                if (it is NetworkException)
                    result.value = NetworkResult.Error(errorResponse = it.errorResponse)
            }

            viewModelScope.launchApi(exceptionHandler) {
                val response = akhtarUseCase.runAkhtarList()
//                result.value = NetworkResult.Success(result = response)
            }
        } else {
            noInternetConnection.value = true
        }
    }

}