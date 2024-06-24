package com.roman.application.home.domain.model.response.city


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("long")
    val long: Double?
)