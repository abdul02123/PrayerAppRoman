package com.roman.application.domain.usecase

import com.roman.application.data.repository.PhotoRepository
import com.roman.application.domain.model.photo.response.PhotoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(private val photoRepository: PhotoRepository){
     suspend fun run(): ArrayList<PhotoResponse> {
        return withContext(Dispatchers.IO) { photoRepository.getAllPhotos() }
    }
}