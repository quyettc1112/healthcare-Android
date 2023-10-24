package com.example.healthcarecomp.di.module

import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.ui.user.login.LoginUseCase
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
    fun provideLoginUseCase(doctorRepository: DoctorRepository): LoginUseCase {
        return LoginUseCase(doctorRepository)
    }

}