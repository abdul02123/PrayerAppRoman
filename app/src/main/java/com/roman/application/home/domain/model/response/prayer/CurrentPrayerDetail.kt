package com.roman.application.home.domain.model.response.prayer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentPrayerDetail(
    val name: String?= null,
    val time: String?= null,
    val nextPrayer: String?= null,
    val nextPrayerTime: String?= null,
    val prayersTime: ArrayList<Prayers>?= null
): Parcelable