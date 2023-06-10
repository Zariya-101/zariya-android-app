package com.zariya.zariya.profile.domain.repository

import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.core.network.NetworkResult

interface ProfileRepository {

    suspend fun updateUser(user: User): NetworkResult<Boolean>
}