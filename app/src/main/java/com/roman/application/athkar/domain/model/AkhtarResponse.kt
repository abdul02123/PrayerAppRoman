package com.roman.application.athkar.domain.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AkhtarResponse(
    @SerializedName("athkars")
    val athkars: ArrayList<Athkar?>?
): Parcelable