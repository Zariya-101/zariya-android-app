package com.zariya.zariya.casting.domain.usecase

import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.domain.repository.CastingOnboardingRepository
import javax.inject.Inject

class CastingOnboardingUseCase @Inject constructor(
    private val castingOnboardingRepository: CastingOnboardingRepository
) {

    suspend fun createActorProfile(actorProfile: ActorProfile) =
        castingOnboardingRepository.createActorProfile(actorProfile)

    suspend fun getActorProfile() = castingOnboardingRepository.getActorProfile()
}