package com.roman.application.athkar.domain.usecase

import com.roman.application.athkar.data.repository.AkhtarRepository
import com.roman.application.athkar.domain.model.AkhtarResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAkhtarUseCase @Inject constructor(private val akhtarRepository: AkhtarRepository){

    suspend fun runAkhtarList(): AkhtarResponse {
        return withContext(Dispatchers.IO) { akhtarRepository.getAkhtar() }
    }
}