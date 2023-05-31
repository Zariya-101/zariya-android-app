package com.zariya.zariya.upload.data.repository

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.upload.domain.repository.UploadRepository
import com.zariya.zariya.utils.ACTOR_PROFILE_IMAGE
import com.zariya.zariya.utils.AGENCY_PROFILE_IMAGE
import com.zariya.zariya.utils.CASTING_CALL_IMAGE
import com.zariya.zariya.utils.FOL_ACTORS
import com.zariya.zariya.utils.FOL_AGENCIES
import com.zariya.zariya.utils.FOL_CASTING_CALL
import com.zariya.zariya.utils.FOL_IMAGES
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadRepositoryImpl @Inject constructor(
    private val preference: AppSharedPreference?,
    private val storageRef: StorageReference
) : UploadRepository {

    override suspend fun uploadImage(imageUri: Uri, imageType: String): NetworkResult<Uri> {
        return try {
            preference?.getUserData()?.id?.let { id ->
                val downloadUrl = getStorageRefWithBucketName(imageType).child(id)
                    .child(UUID.randomUUID().toString())
                    .putFile(imageUri).await()
                    .storage.downloadUrl.await()

                NetworkResult.Success(downloadUrl)
            } ?: run {
                NetworkResult.Error("Something went wrong")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message.toString())
        }
    }

    private fun getStorageRefWithBucketName(imageType: String) = when (imageType) {
        ACTOR_PROFILE_IMAGE -> {
            storageRef.child(FOL_IMAGES).child(FOL_ACTORS)
        }

        AGENCY_PROFILE_IMAGE -> {
            storageRef.child(FOL_IMAGES).child(FOL_AGENCIES)
        }

        CASTING_CALL_IMAGE -> {
            storageRef.child(FOL_IMAGES).child(FOL_CASTING_CALL)
        }

        else -> {
            storageRef.child(FOL_IMAGES)
        }
    }
}