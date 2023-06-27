package com.zariya.zariya.workshop.domain.usecase

import com.zariya.zariya.workshop.data.model.WorkshopLikes
import com.zariya.zariya.workshop.domain.repository.WorkshopRepository
import javax.inject.Inject

class WorkshopUseCase @Inject constructor(
    private val workshopRepository: WorkshopRepository
) {

    suspend fun getWorkshopsList(type: String?) = workshopRepository.getWorkshopsList(type)

    suspend fun likeWorkshop(workshopLikes: WorkshopLikes?, workshopId: String) =
        workshopRepository.likeWorkshop(workshopLikes, workshopId)

    suspend fun isWorkshopLikedByUser(workshopId: String) =
        workshopRepository.isWorkshopLikedByUser(workshopId)
}