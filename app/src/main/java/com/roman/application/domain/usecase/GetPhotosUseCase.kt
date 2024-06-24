package com.roman.application.domain.usecase

import com.roman.application.data.repository.PhotoRepository
import com.roman.application.domain.model.response.city.Cities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(private val photoRepository: PhotoRepository){

    suspend fun runCities(): Cities {
        return withContext(Dispatchers.IO) { photoRepository.getCites() }
    }
}