package com.zariya.zariya.casting.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.domain.repository.CastingOnboardingRepository
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.utils.COL_ACTORS
import com.zariya.zariya.utils.USER_ID
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CastingOnboardingRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val preference: AppSharedPreference?
) : CastingOnboardingRepository {

    override suspend fun createActorProfile(actorProfile: ActorProfile) = try {
        preference?.getUserData()?.let { user ->
            user.id?.let {
                actorProfile.userId = it
                actorProfile.name = user.name ?: "Actor XXXX"
                firestore.collection(COL_ACTORS)
                    .add(actorProfile).await()
                NetworkResult.Success(true)
            }
        } ?: run {
            NetworkResult.Error("Something went wrong")
        }
    } catch (e: Exception) {
        NetworkResult.Error(e.message.toString())
    }

    override suspend fun getActorProfile() = callbackFlow {
        val listener = firestore.collection(COL_ACTORS)
            .whereEqualTo(USER_ID, preference?.getUserData()?.id)
            .get()
            .addOnCompleteListener {
                val result = if (it.isSuccessful) {
                    try {
                        val actor = it.result.documents[0].toObject(ActorProfile::class.java)
                        NetworkResult.Success(actor)
                    } catch (e: Exception) {
                        Log.e("CastingOnbRepoImpl", "Get Actor Exception")
                        NetworkResult.Error(e.message.toString())
                    }
                } else {
                    Log.e("CastingOnbRepoImpl", "Get Actor Not successful")
                    NetworkResult.Error(it.exception?.message.toString())
                }

                trySend(result)
            }

        awaitClose { listener }
    }

    override suspend fun getActors() = callbackFlow {
        val listener = firestore.collection(COL_ACTORS)
            .get()
            .addOnCompleteListener {
                val result = if (it.isSuccessful) {
                    try {
                        val actors =
                            it.result.documents.map { it.toObject(ActorProfile::class.java) }
                        NetworkResult.Success(actors)
                    } catch (e: Exception) {
                        Log.e("CastingOnbRepoImpl", "Get Actors Exception")
                        NetworkResult.Error(e.message.toString())
                    }
                } else {
                    Log.e("CastingOnbRepoImpl", "Get Actors Not successful")
                    NetworkResult.Error(it.exception?.message.toString())
                }
                trySend(result)
            }
        awaitClose {
            listener
        }
    }
}