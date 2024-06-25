package com.roman.application.home.domain.model.response.prayer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentPrayerDetail(
    val name: String,
    val time: String,
    val nextPrayer: String,
    val nextPrayerTime: String,
    val prayersTime: List<String>
): Parcelable