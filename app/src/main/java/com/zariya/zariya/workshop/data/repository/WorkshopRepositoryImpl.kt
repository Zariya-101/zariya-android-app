package com.zariya.zariya.workshop.data.repository

import android.util.Log
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.utils.COL_WORKSHOPS
import com.zariya.zariya.utils.COL_WORKSHOP_LIKES
import com.zariya.zariya.utils.LIKES
import com.zariya.zariya.utils.TYPE
import com.zariya.zariya.utils.USER_ID
import com.zariya.zariya.utils.WORKSHOP_ID
import com.zariya.zariya.workshop.data.model.Workshop
import com.zariya.zariya.workshop.data.model.WorkshopLikes
import com.zariya.zariya.workshop.domain.repository.WorkshopRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkshopRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val preference: AppSharedPreference?
) : WorkshopRepository {

    override suspend fun getWorkshopsList(type: String?) = callbackFlow {
        val query = if (type.isNullOrEmpty().not()) {
            firestore.collection(COL_WORKSHOPS).whereEqualTo(TYPE, type)
        } else {
            firestore.collection(COL_WORKSHOPS)
        }
        val listener = query.get()
            .addOnCompleteListener {
                val result = if (it.isSuccessful) {
                    try {
                        val workshops =
                            it.result.documents.map {
                                val workshop = it.toObject(Workshop::class.java)
                                workshop?.workshopId = it.id
                                return@map workshop
                            }
                        NetworkResult.Success(workshops)
                    } catch (e: Exception) {
                        Log.e("WorkshopRepoImpl", "Get Workshops List Exception")
                        NetworkResult.Error(e.message.toString())
                    }
                } else {
                    Log.e("WorkshopRepoImpl", "Get Workshops List Not successful")
                    NetworkResult.Error(it.exception?.message.toString())
                }
                trySend(result)
            }
        awaitClose { listener }
    }

    override suspend fun likeWorkshop(workshopLikes: WorkshopLikes?, workshopId: String) = try {
        val user = preference?.getUserData()
        user?.id?.let { userId ->
            workshopLikes?.let {
                workshopLikes.mappingId?.let { mappingId ->
                    firestore.collection(COL_WORKSHOP_LIKES)
                        .document(mappingId)
                        .delete()
                        .await()

                    firestore.collection(COL_WORKSHOPS)
                        .document(workshopId)
                        .update(LIKES, FieldValue.increment(-1))
                        .await()

                    NetworkResult.Success(true)
                }
            } ?: run {
                firestore.collection(COL_WORKSHOP_LIKES)
                    .add(WorkshopLikes(userId = userId, workshopId = workshopId))
                    .await()

                firestore.collection(COL_WORKSHOPS)
                    .document(workshopId)
                    .update(LIKES, FieldValue.increment(1))
                    .await()

                NetworkResult.Success(true)
            }
        } ?: run {
            NetworkResult.Error("Something went wrong")
        }
    } catch (e: Exception) {
        Log.e("WorkshopRepositoryImpl", "Like Workshop Exception")
        NetworkResult.Error(e.message.toString())
    }

    override suspend fun isWorkshopLikedByUser(workshopId: String) = callbackFlow {
        val user = preference?.getUserData()
        user?.id?.let { userId ->
            val listener = firestore.collection(COL_WORKSHOP_LIKES)
                .whereEqualTo(USER_ID, userId)
                .whereEqualTo(WORKSHOP_ID, workshopId)
                .get()
                .addOnCompleteListener {
                    val result = if (it.isSuccessful) {
                        if (it.result.documents.size > 0) {
                            val workshopLikes = WorkshopLikes(userId, workshopId)
                            workshopLikes.mappingId = it.result.documents[0].id
                            NetworkResult.Success(workshopLikes)
                        } else {
                            NetworkResult.Success(null)
                        }
                    } else {
                        Log.e("WorkshopRepoImpl", "isWorkshopLikedByUser Not successful")
                        NetworkResult.Error(it.exception?.message.toString())
                    }

                    trySend(result)
                }

            awaitClose { listener }
        } ?: run {
            NetworkResult.Error("Something went wrong")
        }
    }
}