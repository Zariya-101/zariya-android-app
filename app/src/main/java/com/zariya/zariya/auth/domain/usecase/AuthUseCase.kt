package com.zariya.zariya.auth.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.PhoneAuthCredential
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.auth.domain.repository.AuthRepository
import com.zariya.zariya.core.network.NetworkResult
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun authenticateWithPhone(credential: PhoneAuthCredential) = flow {
        authRepository.authenticateWithPhone(credential).collect { phoneAuth ->
            when (phoneAuth) {
                is NetworkResult.Success -> {
                    if (phoneAuth.data?.isNew!!) {
                        authRepository.checkIfUserExists(phoneAuth.data).collect { userCheck ->
                            when (userCheck) {
                                is NetworkResult.Success -> {
                                    if (userCheck.data != null) {
                                        authRepository.deleteCurrentFirebaseUser()
                                        userCheck.data.isNew = false
                                        emit(userCheck as NetworkResult<User>)
                                    } else emit(phoneAuth)
                                }

                                else -> emit(phoneAuth)
                            }
                        }
                    } else emit(phoneAuth)
                }

                else -> emit(phoneAuth)
            }
        }
    }

    suspend fun authenticateWithGoogle(credential: AuthCredential) = flow {
        authRepository.authenticateWithGoogle(credential).collect { googleAuth ->
            when (googleAuth) {
                is NetworkResult.Success -> {
                    if (googleAuth.data?.isNew!!) {
                        authRepository.checkIfUserExists(googleAuth.data).collect { userCheck ->
                            when (userCheck) {
                                is NetworkResult.Success -> {
                                    if (userCheck.data != null) {
                                        authRepository.deleteCurrentFirebaseUser()
                                        userCheck.data.isNew = false
                                        emit(userCheck as NetworkResult<User>)
                                    } else emit(googleAuth)
                                }

                                else -> emit(googleAuth)
                            }
                        }
                    } else emit(googleAuth)
                }

                else -> emit(googleAuth)
            }
        }
    }

    suspend fun authenticateWithFacebook(credential: AuthCredential) =
        authRepository.authenticateWithFacebook(credential)

    suspend fun updateFcmToken(user: User) = authRepository.updateFcmToken(user)

    suspend fun getUserFromDB(authenticatedUser: User) =
        authRepository.getUserFromDB(authenticatedUser)

    suspend fun createUser(authenticatedUser: User) = authRepository.createUser(authenticatedUser)
}