package com.roman.application.athkar.domain.model


import com.google.gson.annotations.SerializedName

data class Athkar(
    @SerializedName("link")
    val link: String?,
    @SerializedName("text")
    val text: String?
)