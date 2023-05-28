package com.zariya.zariya.upload.domain.repository

import android.net.Uri
import com.zariya.zariya.core.network.NetworkResult

interface UploadRepository {

    suspend fun uploadImage(imageUri: Uri, imageType: String): NetworkResult<Uri>
}