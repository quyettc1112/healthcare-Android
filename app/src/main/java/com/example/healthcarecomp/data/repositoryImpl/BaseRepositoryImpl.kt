package com.example.healthcarecomp.data.repositoryImpl

import android.util.Log
import com.example.healthcarecomp.data.repository.BaseRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

open class BaseRepositoryImpl<T> @Inject constructor(
    open val firebaseRef: DatabaseReference
) : BaseRepository<T> {

     var tableName: String? = null
     var entityClass: Class<T>? = null

    operator fun invoke(
        tableName: String,
        entityClass: Class<T>
    ) {
        this.tableName = tableName
        this.entityClass = entityClass
    }

    private val TAG = "BaseRepositoryImpl"
    val _dbRef = tableName?.let { firebaseRef.child(it) }

    override fun getAll(
        listener: (Resource<MutableList<T>>) -> Unit
    ) {
        val query = firebaseRef.child(tableName!!)
        if (query != null) {
            fetchData(query, listener)
        }
    }

    override suspend fun upsert(entity: T, id: String): Resource<T> {
        var result: Resource<T> = Resource.Unknown()
        _dbRef?.child(id)?.setValue(entity)?.addOnCompleteListener {
            result = Resource.Success(entity)
        }?.addOnFailureListener {
            result = Resource.Error(it.message)
        }
        Log.e(TAG, "upsert")
        return result
    }

    override suspend fun remove(entity: T, id: String): Resource<T> {
        var result: Resource<T> = Resource.Unknown()
        _dbRef?.child(id)?.removeValue()?.addOnCompleteListener {
            result = Resource.Success(entity)
        }?.addOnFailureListener {
            result = Resource.Error(it.message)
        }
        return result
    }

    override fun onItemChange(
        listener: (Resource<T>) -> Unit, id: String
    ) {
        val query = _dbRef?.child(id)
        if (query != null) {
            itemFetchData(query, listener)
        }
    }

    private fun itemFetchData(
        query: Query, listener: (Resource<T>) -> Unit
    ) {
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(entityClass!!)
                listener?.let {
                    item?.let {
                        listener(Resource.Success(it))
                        Log.i(TAG, "${item.toString()}")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                listener(Resource.Error(error.message))
            }

        })
    }

    private fun fetchData(
        query: Query, listener: (Resource<MutableList<T>>) -> Unit
    ) {
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<T>()
                snapshot.children.forEach { data ->
                    val item = data.getValue(entityClass!!)
                    item?.let {
                        list.add(it)
                    }
                }
                listener?.let {
                    it(Resource.Success(list))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                listener(Resource.Error(error.message))
            }

        })
    }

}