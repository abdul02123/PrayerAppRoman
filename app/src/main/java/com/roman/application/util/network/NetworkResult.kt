package com.roman.application.util.network

sealed class NetworkResult<T>(
    val result: ArrayList<T>? = null,
    val errorResponse: ErrorResponse? = null,
    val message: String? = null
) {
    class Success<T>(result: ArrayList<T>?) : NetworkResult<T>(result = result)

    class Error<T>(errorResponse: ErrorResponse?) : NetworkResult<T>(errorResponse = errorResponse)

    class Loading<T>() : NetworkResult<T>()
}