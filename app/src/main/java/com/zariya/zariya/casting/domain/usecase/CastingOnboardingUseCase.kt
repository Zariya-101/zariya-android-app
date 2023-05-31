package com.zariya.zariya.casting.domain.usecase

import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.data.model.Agency
import com.zariya.zariya.casting.data.model.CastingCall
import com.zariya.zariya.casting.data.model.Volunteer
import com.zariya.zariya.casting.domain.repository.CastingOnboardingRepository
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CastingOnboardingUseCase @Inject constructor(
    private val castingOnboardingRepository: CastingOnboardingRepository,
    private val preference: AppSharedPreference?
) {

    suspend fun createActorProfile(actorProfile: ActorProfile) =
        castingOnboardingRepository.createActorProfile(actorProfile)

    suspend fun getActorProfile() = castingOnboardingRepository.getActorProfile()

    suspend fun getActors() = castingOnboardingRepository.getActors()

    suspend fun createAgency(agency: Agency) =
        castingOnboardingRepository.createAgencyProfile(agency)

    suspend fun getAllAgencies() = castingOnboardingRepository.getAllAgencies()

    suspend fun getAgencyProfile() = castingOnboardingRepository.getAgencyProfile()

    suspend fun getVolunteersForMyAgency() = flow {
        getAgencyProfile().collect { agencyResult ->
            when (agencyResult) {
                is NetworkResult.Success -> {
                    val agencyId = agencyResult.data?.agencyId
                    castingOnboardingRepository.getVolunteersForMyAgency(agencyId)
                        .collect { volunteers ->
                            when (volunteers) {
                                is NetworkResult.Success -> {
                                    emit(NetworkResult.Success(volunteers))
                                }

                                else -> emit(NetworkResult.Error("Something went wrong"))
                            }
                        }
                }

                else -> emit(NetworkResult.Error("Something went wrong"))
            }
        }
    }

    suspend fun createVolunteer(volunteer: Volunteer) =
        castingOnboardingRepository.createVolunteerProfile(volunteer)

    suspend fun createCastingCall(castingCall: CastingCall) =
        castingOnboardingRepository.createCastingCall(castingCall)

    fun getUserDetails() = preference?.getUserData()
}