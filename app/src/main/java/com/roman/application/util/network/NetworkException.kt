package com.roman.application.util.network

class NetworkException(val errorResponse: ErrorResponse?=null)  : Exception()