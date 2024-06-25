package com.roman.application.home.domain.model.response.prayer

data class PrayerTimesResponse(
    val prayerTimes: Map<String, List<String>>
)