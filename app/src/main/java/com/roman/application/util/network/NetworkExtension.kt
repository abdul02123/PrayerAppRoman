package com.roman.application.util.network

import com.roman.application.R
import com.roman.application.base.BaseApplication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.ContinuationInterceptor

    inline fun CoroutineScope.exceptionHandler(crossinline block: suspend (Throwable) -> Unit) = CoroutineExceptionHandler { _, exception ->
        val mainDispatcher = (coroutineContext[ContinuationInterceptor] as? MainCoroutineDispatcher
            ?: Dispatchers.Main)
        exception.printStackTrace()
        launch(mainDispatcher.immediate) {
            var errorResponse = ErrorResponse()
            if (exception is UnknownHostException) {
                errorResponse.message = BaseApplication.getInstance().getString(R.string.network_something_wrong)
            }
            else if (exception is SocketTimeoutException){
                errorResponse.message = BaseApplication.getInstance().getString(R.string.network_error_message_time_out)
            }
            else{
                errorResponse = (exception as NetworkException).errorResponse?: ErrorResponse()
            }
            block(NetworkException(errorResponse))
        }
    }


    fun CoroutineScope.launchApi(exceptionHandler : CoroutineExceptionHandler?, block: suspend (CoroutineScope) -> Unit) =
        exceptionHandler?.let {
            this.launch(it){
                block.invoke(this)
            }
        }

