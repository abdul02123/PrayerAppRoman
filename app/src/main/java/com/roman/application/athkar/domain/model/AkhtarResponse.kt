package com.roman.application.athkar.domain.model


import com.google.gson.annotations.SerializedName

data class AkhtarResponse(
    @SerializedName("athkars")
    val athkars: ArrayList<Athkar?>?
)