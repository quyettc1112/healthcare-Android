package com.example.healthcarecomp.data.repository

import android.net.Uri

interface ImageRepository {
    suspend fun uploadImage(
        imageUri: Uri,
        onSuccess: ((uri: Uri) -> Unit),
        onFailure: ((errorMessage: String) -> Unit),
        onProgress: (() -> Unit)
    )

    suspend fun downloadImage()
}