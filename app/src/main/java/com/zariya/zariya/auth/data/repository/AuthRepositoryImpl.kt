package com.zariya.zariya.auth.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.zariya.zariya.app
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.auth.domain.repository.AuthRepository
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.utils.COL_USERS
import com.zariya.zariya.utils.FCM_TOKEN
import io.realm.kotlin.mongodb.Credentials
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

    override suspend fun createAccount(email: String, password: String) {
        app.emailPasswordAuth.registerUser(email, password)
    }

    override suspend fun login(email: String, password: String) {
        app.login(Credentials.emailPassword(email, password))
    }

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
}