package com.zariya.zariya.auth.domain.repository

import com.google.firebase.auth.PhoneAuthCredential
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.core.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun authenticateWithPhone(credential: PhoneAuthCredential): Flow<NetworkResult<User>>

    suspend fun updateFcmToken(user: User): NetworkResult<Boolean>

    suspend fun getUserFromDB(authenticatedUser: User): Flow<NetworkResult<User>>

    suspend fun signUpUser(authenticatedUser: User): NetworkResult<Boolean>
}