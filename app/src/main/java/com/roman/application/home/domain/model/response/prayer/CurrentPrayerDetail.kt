package com.roman.application.home.domain.model.response.prayer

data class CurrentPrayerDetail(
    val name: String,
    val time: String,
    val nextPrayer: String,
    val nextPrayerTime: String,
    val prayersTime: PrayersTime
)