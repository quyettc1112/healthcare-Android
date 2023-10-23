package com.example.healthcarecomp.di.module

import android.content.Context
import androidx.room.Room
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.database.AppDatabase
import com.example.healthcarecomp.data.database.dao.DoctorDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, Constant.APP_DATABASE_NAME).build()
    }

    @Provides
    fun provideDoctorDao(appDatabase: AppDatabase): DoctorDao {
        return appDatabase.doctorDao()
    }

}