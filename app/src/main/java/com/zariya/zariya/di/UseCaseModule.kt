package com.zariya.zariya.di

import com.zariya.zariya.auth.domain.repository.AuthRepository
import com.zariya.zariya.auth.domain.usecase.AuthUseCase
import com.zariya.zariya.casting.domain.repository.CastingOnboardingRepository
import com.zariya.zariya.casting.domain.usecase.CastingOnboardingUseCase
import com.zariya.zariya.upload.domain.repository.UploadRepository
import com.zariya.zariya.upload.domain.usecase.UploadUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideAuthUseCase(authRepository: AuthRepository) = AuthUseCase(authRepository)

    @Provides
    @ViewModelScoped
    fun provideCastingUseCase(castingOnboardingRepository: CastingOnboardingRepository) =
        CastingOnboardingUseCase(castingOnboardingRepository)

    @Provides
    @ViewModelScoped
    fun provideUploadUseCase(uploadRepository: UploadRepository) = UploadUseCase(uploadRepository)
}