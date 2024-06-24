package com.roman.application.home.domain.model.response.city


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("file")
    val `file`: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("location")
    val location: Location?,
    @SerializedName("name_ar")
    val nameAr: String?,
    @SerializedName("name_en")
    val nameEn: String?
)