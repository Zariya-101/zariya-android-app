package com.zariya.zariya.workshop.domain.repository

import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.workshop.data.model.Workshop
import com.zariya.zariya.workshop.data.model.WorkshopLikes
import kotlinx.coroutines.flow.Flow

interface WorkshopRepository {

    suspend fun getWorkshopsList(type: String?): Flow<NetworkResult<List<Workshop?>>>

    suspend fun likeWorkshop(workshopLikes: WorkshopLikes?, workshopId: String): NetworkResult<Boolean>

    suspend fun isWorkshopLikedByUser(workshopId: String): Flow<NetworkResult<WorkshopLikes?>>
}