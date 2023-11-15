package com.example.healthcarecomp.di

import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.ChatMessageRepository
import com.example.healthcarecomp.data.repository.ChatRoomRepository
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.data.repository.ImageRepository
import com.example.healthcarecomp.data.repository.MedicalHistoryRepository
import com.example.healthcarecomp.data.repository.NotificationRepository
import com.example.healthcarecomp.data.repository.PatientRepository
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.data.repositoryImpl.AuthRepositoryImpl
import com.example.healthcarecomp.data.repositoryImpl.ChatMessageRepositoryImpl
import com.example.healthcarecomp.data.repositoryImpl.ChatRoomRepositoryImpl
import com.example.healthcarecomp.data.repositoryImpl.DoctorRepositoryImpl
import com.example.healthcarecomp.data.repositoryImpl.ImageRepositoryImpl
import com.example.healthcarecomp.data.repositoryImpl.MedicalHistoryRepositoryImpl
import com.example.healthcarecomp.data.repositoryImpl.NotificationRepositoryImpl
import com.example.healthcarecomp.data.repositoryImpl.PatientRepositoryImpl
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
    abstract fun bindPatientRepository(patientRepositoryImpl: PatientRepositoryImpl): PatientRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMedicalHistoryRepository(medicalHistoryRepositoryImpl: MedicalHistoryRepositoryImpl): MedicalHistoryRepository


    @Binds
    @Singleton
    abstract fun bindScheduleRepository(scheduleRepositoryImpl: ScheduleRepositoryImpl): ScheduleRepository


    @Binds
    @Singleton
    abstract fun bindChatRoomRepository(chatRoomRepositoryImpl: ChatRoomRepositoryImpl): ChatRoomRepository

    @Binds
    @Singleton
    abstract fun bindChatMessageRepository(chatMessageRepositoryImpl: ChatMessageRepositoryImpl): ChatMessageRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository
    
    @Binds
    @Singleton
    abstract fun bindImageRepository(imageRepositoryImpl: ImageRepositoryImpl): ImageRepository

}