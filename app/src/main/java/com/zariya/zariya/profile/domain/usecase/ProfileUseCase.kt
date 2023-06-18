package com.zariya.zariya.profile.domain.usecase

import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val preference: AppSharedPreference?
) {

    fun getUser() = preference?.getUserData()

    fun updateStoredUser(user: User) = preference?.setUserData(user)

    suspend fun updateUser(user: User) = profileRepository.updateUser(user)
}