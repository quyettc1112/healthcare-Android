package com.example.healthcarecomp.data.model

data class Attachment(
    val type: String? = null,
    val filePath: String? = null
) {
    companion object {
        const val TYPE_IMAGE = "TYPE_IMAGE"
        const val TYPE_FILE = "TYPE_FILE"
    }
}