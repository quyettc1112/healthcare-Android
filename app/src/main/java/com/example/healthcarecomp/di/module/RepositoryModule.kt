package com.example.healthcarecomp.di.module

import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.data.repositoryImpl.DoctorRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDoctorRepository(doctorRepositoryImpl: DoctorRepositoryImpl): DoctorRepository
}