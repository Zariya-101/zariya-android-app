package com.zariya.zariya.casting.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.data.model.Agency
import com.zariya.zariya.casting.data.model.Volunteer
import com.zariya.zariya.casting.domain.repository.CastingOnboardingRepository
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.utils.ACTOR
import com.zariya.zariya.utils.AGENCY
import com.zariya.zariya.utils.COL_ACTORS
import com.zariya.zariya.utils.COL_AGENCIES
import com.zariya.zariya.utils.COL_USERS
import com.zariya.zariya.utils.COL_VOLUNTEERS
import com.zariya.zariya.utils.ROLE
import com.zariya.zariya.utils.USER_ID
import com.zariya.zariya.utils.VOLUNTEER
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
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

                firestore.collection(COL_USERS).document(it).update(ROLE, ACTOR).await()

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

    override suspend fun createAgencyProfile(agency: Agency) = try {
        preference?.getUserData()?.id?.let { id ->
            agency.userId = id
            firestore.collection(COL_AGENCIES)
                .add(agency).await()

            firestore.collection(COL_USERS).document(id).update(ROLE, AGENCY).await()

            NetworkResult.Success(true)
        } ?: run {
            NetworkResult.Error("Something went wrong")
        }
    } catch (e: Exception) {
        NetworkResult.Error(e.message.toString())
    }

    override suspend fun getAllAgencies() = callbackFlow {
        val listener = firestore.collection(COL_AGENCIES)
            .get()
            .addOnCompleteListener {
                val result = if (it.isSuccessful) {
                    try {
                        val agencies =
                            it.result.documents.map {
                                val agency = it.toObject(Agency::class.java)
                                agency?.agencyId = it.id
                                return@map agency
                            }
                        NetworkResult.Success(agencies)
                    } catch (e: Exception) {
                        Log.e("CastingOnbRepoImpl", "Get Actors Exception")
                        NetworkResult.Error(e.message.toString())
                    }
                } else {
                    Log.e("CastingOnbRepoImpl", "Get Agencies Not successful")
                    NetworkResult.Error(it.exception?.message.toString())
                }

                trySend(result)
            }

        awaitClose { listener }
    }

    override suspend fun createVolunteerProfile(volunteer: Volunteer) = try {
        preference?.getUserData()?.id?.let { id ->
            volunteer.userId = id
            firestore.collection(COL_VOLUNTEERS)
                .add(volunteer).await()

            firestore.collection(COL_USERS).document(id).update(ROLE, VOLUNTEER).await()

            NetworkResult.Success(true)
        } ?: run {
            NetworkResult.Error("Something went wrong")
        }
    } catch (e: Exception) {
        NetworkResult.Error(e.message.toString())
    }
}