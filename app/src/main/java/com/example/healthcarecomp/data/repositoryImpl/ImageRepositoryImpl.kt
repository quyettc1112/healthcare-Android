package com.example.healthcarecomp.data.repositoryImpl

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.ImageRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import dagger.Component
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
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
        val storageRef = firebaseStorage.reference.child("images/${System.currentTimeMillis()}.${getFileExtension(imageUri)}")
        val uploadTask: UploadTask = storageRef.putFile(imageUri)
        uploadTask.addOnProgressListener {
            onProgress()
        }.addOnCompleteListener{
            if(it.isSuccessful){
                onSuccess(Uri.parse(it.result.toString()))
            } else {
                onFailure("Upload image failed")
            }
        }.addOnCanceledListener{
            onFailure("Upload image canceled")
        }
    }

    private fun getFileExtension(uri: Uri): String?{
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    override suspend fun downloadImage() {
        TODO("Not yet implemented")
    }
}