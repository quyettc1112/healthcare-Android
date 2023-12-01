package com.example.healthcarecomp.data.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData

interface FileUploadRepository {
    suspend fun uploadFile(
        folder: String,
        fileUri: Uri,
        onSuccess: ((uri: Uri) -> Unit),
        onFailure: ((errorMessage: String) -> Unit),
        onProgress: ((MutableLiveData<Int>) -> Unit)
    )

}