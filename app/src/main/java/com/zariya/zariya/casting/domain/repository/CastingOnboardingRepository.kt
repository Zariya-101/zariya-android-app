package com.zariya.zariya.casting.domain.repository

import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.data.model.Agency
import com.zariya.zariya.core.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CastingOnboardingRepository {

    suspend fun createActorProfile(actorProfile: ActorProfile): NetworkResult<Boolean>

    suspend fun getActorProfile(): Flow<NetworkResult<ActorProfile>>

    suspend fun getActors(): Flow<NetworkResult<List<ActorProfile?>>>

    suspend fun createAgencyProfile(agency: Agency): NetworkResult<Boolean>
}