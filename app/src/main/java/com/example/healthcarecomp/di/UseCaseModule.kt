package com.example.healthcarecomp.di

import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.ChatMessageRepository
import com.example.healthcarecomp.data.repository.ChatRoomRepository
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.data.repository.FileUploadRepository
import com.example.healthcarecomp.data.repository.MedicalHistoryRepository
import com.example.healthcarecomp.data.repository.NotificationRepository
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.ui.medicalhistory.MedicalHistoryUseCase
import com.example.healthcarecomp.ui.schedule.ScheduleUseCase
import com.example.healthcarecomp.ui.auth.login.LoginUseCase
import com.example.healthcarecomp.ui.chat.ChatUseCase
import com.example.healthcarecomp.ui.chatmessage.ChatMessageUseCase
import com.example.healthcarecomp.ui.viewProfile.ViewProfileUserCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideMedicalHistoryUseCase(medicalHistoryRepository: MedicalHistoryRepository, doctorRepository: DoctorRepository): MedicalHistoryUseCase {
        return MedicalHistoryUseCase(medicalHistoryRepository, doctorRepository)
    }

    @Provides
    @Singleton
    fun provineScheduleUseCase(scheduleRepository: ScheduleRepository, doctorRepository: DoctorRepository) : ScheduleUseCase {
        return ScheduleUseCase(scheduleRepository, doctorRepository)
    }


    @Provides
    @Singleton
    fun provideChatRoomUseCase(chatRoomRepository: ChatRoomRepository, authRepository: AuthRepository, chatMessageRepository: ChatMessageRepository) : ChatUseCase {
        return ChatUseCase(chatRoomRepository, authRepository, chatMessageRepository)
    }

    @Provides
    @Singleton
    fun provideChatMessageUseCase(chatMessageRepository: ChatMessageRepository, chatRoomRepository: ChatRoomRepository, notificationRepository: NotificationRepository, fileUploadRepository: FileUploadRepository) : ChatMessageUseCase {
        return ChatMessageUseCase(chatMessageRepository, chatRoomRepository, notificationRepository, fileUploadRepository)
    }

    @Provides
    @Singleton
    fun provideViewProfileUseCase(chatRoomRepository: ChatRoomRepository) : ViewProfileUserCase {
        return ViewProfileUserCase(chatRoomRepository)
    }

}