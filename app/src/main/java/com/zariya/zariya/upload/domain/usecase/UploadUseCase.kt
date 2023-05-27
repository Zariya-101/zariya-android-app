package com.zariya.zariya.upload.domain.usecase

import android.net.Uri
import com.zariya.zariya.upload.domain.repository.UploadRepository
import javax.inject.Inject

class UploadUseCase @Inject constructor(
    private val uploadRepository: UploadRepository
) {

    suspend fun uploadImage(imageUri: Uri) = uploadRepository.uploadImage(imageUri)
}