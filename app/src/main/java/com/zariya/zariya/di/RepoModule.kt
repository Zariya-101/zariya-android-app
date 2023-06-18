package com.zariya.zariya.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.zariya.zariya.auth.data.repository.AuthRepositoryImpl
import com.zariya.zariya.auth.domain.repository.AuthRepository
import com.zariya.zariya.casting.data.repository.CastingOnboardingRepositoryImpl
import com.zariya.zariya.casting.domain.repository.CastingOnboardingRepository
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.profile.data.repository.ProfileRepositoryImpl
import com.zariya.zariya.profile.domain.repository.ProfileRepository
import com.zariya.zariya.upload.data.repository.UploadRepositoryImpl
import com.zariya.zariya.upload.domain.repository.UploadRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Calendar

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        appSharedPreference: AppSharedPreference?,
        calendar: Calendar
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth, firestore, appSharedPreference, calendar)

    @Provides
    fun provideCastingRepository(
        firestore: FirebaseFirestore,
        appSharedPreference: AppSharedPreference?
    ): CastingOnboardingRepository = CastingOnboardingRepositoryImpl(firestore, appSharedPreference)

    @Provides
    fun provideUploadRepository(
        preference: AppSharedPreference?,
        storageRef: StorageReference
    ): UploadRepository = UploadRepositoryImpl(preference, storageRef)

    @Provides
    fun provideProfileRepository(
        firestore: FirebaseFirestore,
        preference: AppSharedPreference?
    ): ProfileRepository = ProfileRepositoryImpl(firestore, preference)
}