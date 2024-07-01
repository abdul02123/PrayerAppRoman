package com.roman.application.athkar.domain.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Athkar(
    @SerializedName("link")
    val link: String?,
    @SerializedName("text")
    val text: String?
): Parcelable