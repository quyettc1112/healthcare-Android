package com.example.healthcarecomp.di

import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.data.repository.MedicalHistoryRepository
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.ui.medicalhistory.MedicalHistoryUseCase
import com.example.healthcarecomp.ui.schedule.ScheduleUseCase
import com.example.healthcarecomp.ui.auth.login.LoginUseCase
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

}