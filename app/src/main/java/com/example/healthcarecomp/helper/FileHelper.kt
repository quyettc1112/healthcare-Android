package com.example.healthcarecomp.helper

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import java.io.File

class FileHelper {

    companion object {
        const val FOLDER_DOWNLOAD = "healcarecomp"

        fun getFileExtension(uri: Uri, context: Context): String? {
            val contentResolver: ContentResolver = context.contentResolver
            val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
        }

        fun getFileName(uri: Uri?, context: Context): String {
            var fileName = ""
            when (uri?.scheme) {
                "content" -> {
                    val documentFile = DocumentFile.fromSingleUri(context, uri)
                    documentFile?.let {
                        fileName = documentFile.name.toString()
                    }
                }

                "file" -> {
                    fileName = uri.lastPathSegment.toString()
                }
            }
            return fileName
        }

        fun getFileSize(uri: Uri?, context: Context): String {
            var fileSize: Long = 0
            when (uri?.scheme) {
                "content" -> {
                    val documentFile = DocumentFile.fromSingleUri(context, uri)
                    documentFile?.let {
                        fileSize = documentFile.length()
                    }
                }

                "file" -> {
                    val path = uri.path
                    path?.let {
                        val file = File(path)
                        fileSize = file.length()
                    }
                }
            }
            val fi = formatFileSize(fileSize)
            Log.i("test", fi)
            return fi
        }

        fun formatFileSize(fileSize: Long): String {
            val KB = 1024
            val MB = KB * 1024
            val GB = MB * 1024
            return if (fileSize < KB) {
                "$fileSize B"
            } else if (fileSize < MB) {
                String.format("%.2f KB", fileSize.toFloat() / KB)
            } else if (fileSize < GB) {
                String.format("%.2f MB", fileSize.toFloat() / MB)
            } else {
                String.format("%.2f GB", fileSize.toFloat() / GB)
            }
        }

        fun downloadFile(url: String, context: Context, fileName: String) {
            val folder = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                FOLDER_DOWNLOAD
            )
            if (!folder.exists()) {
                folder.mkdir()
            } else {
                val file = File(folder, fileName)
                if (file.exists()) {
                    Toast.makeText(context, "File already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val filePath = Uri.fromFile(file)
                    val request = DownloadManager.Request(Uri.parse(url))
                    val downloadManager =
                        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    request.setDestinationUri(filePath)
                    request.setTitle(fileName)
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                    downloadManager.enqueue(request)
                    Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

}