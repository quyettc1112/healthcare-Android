package com.example.healthcarecomp.data.repositoryImpl

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.data.repository.FileUploadRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import javax.inject.Inject

class FileUploadRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val applicationContext: Context
) : FileUploadRepository{

    private val storageRef = firebaseStorage.reference

    override suspend fun uploadFile(
        folder: String,
        fileUri: Uri,
        onSuccess: (uri: Uri) -> Unit,
        onFailure: (errorMessage: String) -> Unit,
        onProgress: (MutableLiveData<Int>) -> Unit
    ) {
        val fileName = "${System.currentTimeMillis()}.${
            getFileExtension(fileUri)
        }"
        val fileRef = storageRef.child("$folder/$fileName")
        val uploadTask = fileRef.putFile(fileUri)
        uploadTask.addOnProgressListener { taskSnapshot ->
            suspend {
                var progress = 0
                val progressLiveData = MutableLiveData(0)
                while (progress<100){
                    progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    progressLiveData.value = progress
                    onProgress(progressLiveData)
                    delay(100)
                }
            }
        }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fileRef.downloadUrl.addOnSuccessListener { uri ->
                        onSuccess.invoke(uri)
                    }
                } else {
                    onFailure.invoke("upload failed: ${task.exception?.message}")
                }
            }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

}