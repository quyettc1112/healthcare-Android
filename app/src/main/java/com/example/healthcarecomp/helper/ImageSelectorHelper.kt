package com.example.healthcarecomp.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class ImageSelectorHelper {
    private val permissionToRequest = mutableListOf<String>()

    companion object {
        const val PERMISSION_REQUEST_CODE = 10
        const val CAMERA_REQUEST_CODE = 101
        const val GALLERY_REQUEST_CODE = 102
    }

    fun checkUserPermission(context: Context): Boolean {
        val permissions = listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionToRequest.add(permission)
            }
        }
        return permissionToRequest.size == 0
    }

    fun requestPermission(activity: AppCompatActivity) {
        if (permissionToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                activity, permissionToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }


    fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        // Save the bitmap to a temporary file
        val contentResolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        val uri: Uri? =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            val outputStream = contentResolver.openOutputStream(it)
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)
            } finally {
                outputStream?.close()
            }
        }

        return uri ?: Uri.EMPTY
    }

}