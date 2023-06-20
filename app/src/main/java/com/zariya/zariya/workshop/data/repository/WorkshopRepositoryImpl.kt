package com.zariya.zariya.workshop.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.utils.COL_WORKSHOPS
import com.zariya.zariya.workshop.data.model.Workshop
import com.zariya.zariya.workshop.domain.repository.WorkshopRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkshopRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val preference: AppSharedPreference?
) : WorkshopRepository {

    override suspend fun getWorkshopsList() = callbackFlow {
        val listener = firestore.collection(COL_WORKSHOPS)
            .get()
            .addOnCompleteListener {
                val result = if (it.isSuccessful) {
                    try {
                        val workshops =
                            it.result.documents.map { it.toObject(Workshop::class.java) }
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
}