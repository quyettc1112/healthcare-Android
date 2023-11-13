package com.example.healthcarecomp.data.repositoryImpl

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.healthcarecomp.data.repository.ImageRepository
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject


class ImageRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val applicationContext: Context
) : ImageRepository {

    override suspend fun uploadImage(
        imageUri: Uri,
        onSuccess: (uri: Uri) -> Unit,
        onFailure: (errorMessage: String) -> Unit,
        onProgress: () -> Unit
    ) {
        val storageRef = firebaseStorage.reference.child(
            "images/${System.currentTimeMillis()}.${
                getFileExtension(imageUri)
            }"
        )
        val uploadTask = storageRef.putFile(imageUri)
        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress =
                (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            onProgress.invoke()
        }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        onSuccess.invoke(uri)
                    }
                } else {
                    onFailure.invoke("Image upload failed: ${task.exception?.message}")
                }
            }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override suspend fun downloadImage() {
        TODO("Not yet implemented")
    }
}