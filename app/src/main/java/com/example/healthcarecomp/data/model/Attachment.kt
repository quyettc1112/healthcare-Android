package com.example.healthcarecomp.data.model

import android.content.Context
import android.net.Uri
import kotlin.reflect.typeOf
import com.example.healthcarecomp.R

data class Attachment(
    val type: String? = null,
    val filePath: String? = null,
    val fileName: String? = null,
    val fileSize: String? = null
) {
    companion object {
        const val TYPE_IMAGE = "TYPE_IMAGE"
        const val TYPE_FILE = "TYPE_FILE"
        const val TYPE_VIDEO = "TYPE_VIDEO"
        const val TYPE_ZIP = "TYPE_ZIP"
        const val TYPE_EXCEL = "TYPE_EXCEL"
        const val TYPE_WORD = "TYPE_WORD"
        const val TYPE_TXT = "TYPE_TXT"
        const val TYPE_AUDIO = "TYPE_AUDIO"
        const val TYPE_PDF = "TYPE_PDF"

        val VIDEO_ICON = R.drawable.ic_video
        val ZIP_ICON = R.drawable.ic_zip_file
        val EXCEL_ICON = R.drawable.ic_microsoft_excel
        val WORD_ICON = R.drawable.ic_microsoft_word
        val TXT_ICON = R.drawable.ic_txt_file
        val AUDIO_ICON = R.drawable.ic_audio_file
        val PDF_ICON = R.drawable.ic_file_pdf

        fun getType(uri: Uri, context: Context): String {
            val mimeType = context.contentResolver.getType(uri)
            mimeType?.let { type ->
                return when {
                    type.startsWith("image/") -> TYPE_IMAGE
                    type.startsWith("video/") -> TYPE_VIDEO
                    type.startsWith("audio/") -> TYPE_AUDIO
                    type.startsWith("text/") -> TYPE_TXT
                    type == "application/msword" -> TYPE_WORD
                    type == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> TYPE_WORD
                    type == "application/pdf" -> TYPE_PDF
                    type == "application/vnd.ms-excel" -> TYPE_EXCEL
                    type == "application/x-rar-compressed" -> TYPE_ZIP
                    type == "application/zip" -> TYPE_ZIP
                    else -> TYPE_ZIP
                }
            }
            return TYPE_ZIP
        }
    }
    fun getFileDisplay(): Int {
        if(type != null && type != TYPE_IMAGE) {
            return when(type) {
                TYPE_VIDEO -> VIDEO_ICON
                TYPE_ZIP -> ZIP_ICON
                TYPE_EXCEL -> EXCEL_ICON
                TYPE_WORD -> WORD_ICON
                TYPE_TXT -> TXT_ICON
                TYPE_AUDIO -> AUDIO_ICON
                TYPE_PDF -> PDF_ICON
                else -> ZIP_ICON
            }
        }
        return -1
    }
}