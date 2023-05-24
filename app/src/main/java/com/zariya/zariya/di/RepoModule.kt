package com.zariya.zariya.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zariya.zariya.auth.data.repository.AuthRepositoryImpl
import com.zariya.zariya.auth.domain.repository.AuthRepository
import com.zariya.zariya.casting.data.repository.CastingOnboardingRepositoryImpl
import com.zariya.zariya.casting.domain.repository.CastingOnboardingRepository
import com.zariya.zariya.core.local.AppSharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        appSharedPreference: AppSharedPreference?
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth, firestore, appSharedPreference)

    @Provides
    fun provideCastingRepository(
        firestore: FirebaseFirestore,
        appSharedPreference: AppSharedPreference?
    ): CastingOnboardingRepository = CastingOnboardingRepositoryImpl(firestore, appSharedPreference)
}