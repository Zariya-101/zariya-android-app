package com.zariya.zariya.auth.data.repository

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.auth.domain.repository.AuthRepository
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.utils.COL_USERS
import com.zariya.zariya.utils.EMAIL
import com.zariya.zariya.utils.FCM_TOKEN
import com.zariya.zariya.utils.PHONE
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val preference: AppSharedPreference?
) : AuthRepository {

    override suspend fun authenticateWithPhone(credential: PhoneAuthCredential) = callbackFlow {
        val listener = firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                val result: NetworkResult<User> = if (it.isSuccessful) {
                    Log.d("AuthRepositoryImpl", it.toString())
                    val isNewUser = it.result?.additionalUserInfo?.isNewUser
                    val currentUser = firebaseAuth.currentUser
                    currentUser?.let {
                        val user = User(id = currentUser.uid, phone = currentUser.phoneNumber)
                        user.isNew = isNewUser
                        NetworkResult.Success(user)
                    } ?: run {
                        NetworkResult.Error("Something went wrong")
                    }
                } else {
                    Log.e("AuthRepositoryImpl", "Phone Auth Not successful")
                    NetworkResult.Error(it.exception?.message.toString())
                }
                trySend(result)
            }
        awaitClose {
            listener
        }
    }

    override suspend fun checkIfUserExists(authenticatedUser: User) = callbackFlow {
        val filter: Filter = when {
            authenticatedUser.phone.isNullOrEmpty().not()
                    && authenticatedUser.email.isNullOrEmpty().not() -> {
                Filter.or(
                    Filter.equalTo(PHONE, authenticatedUser.phone?.substring(3)),
                    Filter.equalTo(EMAIL, authenticatedUser.email)
                )
            }

            authenticatedUser.phone.isNullOrEmpty().not() -> {
                Filter.equalTo(PHONE, authenticatedUser.phone?.substring(3))
            }

            authenticatedUser.email.isNullOrEmpty().not() -> {
                Filter.equalTo(EMAIL, authenticatedUser.email)
            }

            else -> {
                Filter()
            }
        }
        val listener = firestore.collection(COL_USERS)
            .where(filter)
            .get()
            .addOnCompleteListener {
                val result = if (it.isSuccessful) {
                    if (it.result.documents.size > 0) {
                        val user = it.result.documents[0].toObject(User::class.java)
                        NetworkResult.Success(user)
                    } else {
                        NetworkResult.Success(null)
                    }
                } else {
                    NetworkResult.Error("Something went wrong")
                }
                trySend(result)
            }

        awaitClose { listener }
    }

    override suspend fun deleteCurrentFirebaseUser() {
        firebaseAuth.currentUser?.delete()?.addOnCompleteListener {
            val result = if (it.isSuccessful) {
                NetworkResult.Success(true)
            } else {
                NetworkResult.Error("Something went wrong")
            }
        }
    }

    override suspend fun authenticateWithGoogle(credential: AuthCredential) = callbackFlow {
        val listener = firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                val result = if (it.isSuccessful) {
                    Log.d("AuthRepositoryImpl", it.toString())
                    val isNewUser = it.result?.additionalUserInfo?.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    firebaseUser?.let {
                        val user = User(
                            id = firebaseUser.uid,
                            name = firebaseUser.displayName,
                            email = firebaseUser.email
                        )
                        user.isNew = isNewUser
                        NetworkResult.Success(user)
                    } ?: run {
                        NetworkResult.Error("Something went wrong")
                    }
                } else {
                    Log.e("AuthRepositoryImpl", "Google Auth Not successful")
                    NetworkResult.Error(it.exception?.message.toString())
                }
                trySend(result)
            }
        awaitClose {
            listener
        }
    }

    override suspend fun authenticateWithFacebook(credential: AuthCredential) = callbackFlow {
        val listener = firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                val result = if (it.isSuccessful) {
                    Log.d("AuthRepositoryImpl", it.toString())
                    val isNewUser = it.result?.additionalUserInfo?.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    firebaseUser?.let {
                        val user = User(
                            id = firebaseUser.uid,
                            name = firebaseUser.displayName,
                            email = firebaseUser.email
                        )
                        user.isNew = isNewUser
                        NetworkResult.Success(user)
                    } ?: run {
                        NetworkResult.Error("Something went wrong")
                    }
                } else {
                    Log.e("AuthRepositoryImpl", "Facebook Auth Not successful")
                    NetworkResult.Error(it.exception?.message.toString())
                }
                trySend(result)
            }
        awaitClose {
            listener
        }
    }

    override suspend fun updateFcmToken(user: User): NetworkResult<Boolean> = try {
        val fcmToken = preference?.getFcmToken()
        user.id?.let {
            firestore.collection(COL_USERS)
                .document(it)
                .update(FCM_TOKEN, fcmToken)
                .await()
            NetworkResult.Success(true)
        } ?: run {
            NetworkResult.Error("Something went wrong")
        }
    } catch (e: Exception) {
        Log.e("AuthRepositoryImpl", "Update FCM token Exception")
        NetworkResult.Error(e.message.toString())
    }

    override suspend fun getUserFromDB(authenticatedUser: User) = callbackFlow {
        val listener = firestore.collection(COL_USERS)
            .document(authenticatedUser.id!!)
            .get()
            .addOnCompleteListener {
                val result = if (it.isSuccessful) {
                    try {
                        val user = it.result.toObject(User::class.java)
                        NetworkResult.Success(user)
                    } catch (e: Exception) {
                        Log.e("AuthRepositoryImpl", " Get User Exception")
                        NetworkResult.Error(e.message.toString())
                    }
                } else {
                    Log.e("AuthRepositoryImpl", " Get User Not successful")
                    NetworkResult.Error(it.exception?.message.toString())
                }
                trySend(result)
            }
        awaitClose {
            listener
        }
    }

    override suspend fun createUser(authenticatedUser: User): NetworkResult<Boolean> = try {
        authenticatedUser.id?.let { id ->
            firestore.collection(COL_USERS)
                .document(id)
                .set(authenticatedUser)
                .await()

            NetworkResult.Success(true)
        } ?: run {
            NetworkResult.Error("Something went wrong")
        }
    } catch (e: Exception) {
        Log.e("AuthRepositoryImpl", "Signup User Exception")
        NetworkResult.Error(e.message.toString())
    }
}