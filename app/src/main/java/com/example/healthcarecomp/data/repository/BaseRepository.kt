package com.example.healthcarecomp.data.repository

import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.util.Resource

interface BaseRepository<T> {

    suspend fun upsert(entity: T, id: String): Resource<T>
    suspend fun remove(entity: T, id: String): Resource<T>

    fun onItemChange(itemId: String, listener: (Resource<T>) -> Unit, entityClass: Class<T>)
    suspend fun getAll(listener: (Resource<MutableList<T>>) -> Unit ,entityClass: Class<T>)

}