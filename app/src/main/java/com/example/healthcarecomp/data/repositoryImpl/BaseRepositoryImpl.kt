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

class BaseRepositoryImpl<T> @Inject constructor(
    private val firebaseRef: DatabaseReference,
) : BaseRepository<T> {
    private val TAG = "BaseRepositoryImpl"
    lateinit var tableName: String

    operator fun invoke(tableName: String) {
        this.tableName = tableName
    }

    private val _dbRef = firebaseRef.child(tableName)

    override suspend fun getAll(
        listener: (Resource<MutableList<T>>) -> Unit, entityClass: Class<T>
    ) {
        val query = _dbRef
        fetchData(query, listener, entityClass)
    }

    override suspend fun upsert(entity: T, id: String): Resource<T> {
        var result: Resource<T> = Resource.Unknown()
        _dbRef.child(id).setValue(entity).addOnCompleteListener {
            result = Resource.Success(entity)
        }.addOnFailureListener {
            result = Resource.Error(it.message)
        }
        Log.e(TAG, "upsert")
        return result
    }

    override suspend fun remove(entity: T, id: String): Resource<T> {
        var result: Resource<T> = Resource.Unknown()
        _dbRef.child(id).removeValue().addOnCompleteListener {
            result = Resource.Success(entity)
        }.addOnFailureListener {
            result = Resource.Error(it.message)
        }
        return result
    }

    override fun onItemChange(
        itemId: String, listener: (Resource<T>) -> Unit, entityClass: Class<T>
    ) {
        val query = _dbRef.child(itemId)
        itemFetchData(query, listener, entityClass)
    }

    private fun itemFetchData(
        query: Query, listener: (Resource<T>) -> Unit, entityClass: Class<T>
    ) {
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(entityClass)
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
        query: Query, listener: (Resource<MutableList<T>>) -> Unit, entityClass: Class<T>
    ) {
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<T>()
                snapshot.children.forEach { data ->
                    val item = data.getValue(entityClass)
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