package com.example.healthcarecomp.util

import com.example.healthcarecomp.common.Constant

sealed class Resource<T>(
    var data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: String? = Constant.DEFAULT_ERROR_MESSAGE, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
    class Unknown<T> : Resource<T>()
}