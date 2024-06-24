package com.roman.application.home.domain.usecase

import com.roman.application.home.data.repository.HomeRepository
import com.roman.application.home.domain.model.response.city.Cities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(private val photoRepository: HomeRepository){

    suspend fun runCities(): Cities {
        return withContext(Dispatchers.IO) { photoRepository.getCites() }
    }
}