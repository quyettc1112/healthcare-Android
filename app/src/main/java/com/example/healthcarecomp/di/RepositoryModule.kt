package com.example.healthcarecomp.di

import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.data.repository.MedicalHistoryRepository
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.data.repositoryImpl.AuthRepositoryImpl
import com.example.healthcarecomp.data.repositoryImpl.DoctorRepositoryImpl
import com.example.healthcarecomp.data.repositoryImpl.MedicalHistoryRepositoryImpl
import com.example.healthcarecomp.data.repositoryImpl.ScheduleRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMedicalHistoryRepository(medicalHistoryRepositoryImpl: MedicalHistoryRepositoryImpl): MedicalHistoryRepository


    @Binds
    @Singleton
    abstract fun bindScheduleRepository(scheduleRepositoryImpl: ScheduleRepositoryImpl): ScheduleRepository

}