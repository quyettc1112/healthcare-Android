package com.example.healthcarecomp.data.repository

import com.example.healthcarecomp.util.Resource

interface BaseRepository<T> {
    suspend fun upsert(entity: T, id: String): Resource<T>
    suspend fun remove(entity: T, id: String): Resource<T>

    fun onItemChange(listener: (Resource<T>) -> Unit, id: String)
    fun getAll(listener: (Resource<MutableList<T>>) -> Unit)

}