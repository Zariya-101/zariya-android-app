package com.zariya.zariya.profile.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.profile.domain.repository.ProfileRepository
import com.zariya.zariya.utils.COL_USERS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val preference: AppSharedPreference?
) : ProfileRepository {

    override suspend fun updateUser(user: User) = try {
        user.id?.let {
            firestore.collection(COL_USERS)
                .document(it)
                .set(user)
                .await()

            NetworkResult.Success(true)
        } ?: run {
            NetworkResult.Error("Something went wrong")
        }
    } catch (e: Exception) {
        Log.e("ProfileRepositoryImpl", "Update User Exception")
        NetworkResult.Error(e.message.toString())
    }
}