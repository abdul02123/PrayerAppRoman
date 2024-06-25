package com.roman.application.home.domain.usecase

import com.roman.application.home.data.repository.HomeRepository
import com.roman.application.home.domain.model.response.prayer.PrayerTimesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPrayerTimeUseCase @Inject constructor(private val photoRepository: HomeRepository) {
    suspend fun runPrayerTimes(city: String): PrayerTimesResponse {
        return withContext(Dispatchers.IO) { photoRepository.getPrayerTimes(city) }
    }
}