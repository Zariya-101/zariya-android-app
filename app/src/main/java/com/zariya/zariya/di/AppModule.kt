package com.zariya.zariya.di

import android.content.Context
import com.zariya.zariya.core.local.AppSharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providePreference(@ApplicationContext context: Context): AppSharedPreference? =
        AppSharedPreference.getInstance(context)

}