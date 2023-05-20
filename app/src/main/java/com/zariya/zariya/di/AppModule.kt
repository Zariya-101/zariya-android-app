package com.zariya.zariya.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zariya.zariya.auth.data.repository.AuthRepositoryImpl
import com.zariya.zariya.auth.domain.repository.AuthRepository
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
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun providePreference(@ApplicationContext context: Context): AppSharedPreference? =
        AppSharedPreference.getInstance(context)

    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        appSharedPreference: AppSharedPreference?
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth, firestore, appSharedPreference)

}