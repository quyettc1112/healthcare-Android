package com.example.healthcarecomp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.healthcarecomp.data.sharePreference.AppSharePreference
import com.example.healthcarecomp.util.session.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext applicationContext: Context): Context {
        return applicationContext
    }

    @Provides
    @Singleton
     fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences{
        return context.getSharedPreferences(AppSharePreference.APP_SHARE_KEY,Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppSharePreference(@ApplicationContext context: Context) : AppSharePreference {
        return AppSharePreference(context)
    }

    @Provides
    @Singleton
    fun provideSessionManager(appSharePreference: AppSharePreference) : SessionManager {
        return SessionManager(appSharePreference)
    }
}