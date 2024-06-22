package com.roman.application.domain.model.photo.response


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PhotoResponse(
    val albumId: Int?,
    val id: Int?,
    val thumbnailUrl: String?,
    val title: String?,
    val url: String?
) : Parcelable