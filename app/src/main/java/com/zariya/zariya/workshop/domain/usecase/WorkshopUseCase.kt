package com.zariya.zariya.workshop.domain.usecase

import com.zariya.zariya.workshop.domain.repository.WorkshopRepository
import javax.inject.Inject

class WorkshopUseCase @Inject constructor(
    private val workshopRepository: WorkshopRepository
) {

    suspend fun getWorkshopsList() = workshopRepository.getWorkshopsList()
}