package com.example.healthcarecomp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ConvertUtils {
    fun convertLongToDateString(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun convertDateStringToLong(dateString: String): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        return date?.time ?: 0L
    }
}