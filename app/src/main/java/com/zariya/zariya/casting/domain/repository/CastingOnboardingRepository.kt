package com.zariya.zariya.casting.domain.repository

import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.core.network.NetworkResult

interface CastingOnboardingRepository {

    suspend fun createActorProfile(actorProfile: ActorProfile): NetworkResult<Boolean>
}