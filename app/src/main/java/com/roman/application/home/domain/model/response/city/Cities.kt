package com.roman.application.home.domain.model.response.city


import com.google.gson.annotations.SerializedName

data class Cities(
    @SerializedName("cities")
    val cities: ArrayList<City>?
)