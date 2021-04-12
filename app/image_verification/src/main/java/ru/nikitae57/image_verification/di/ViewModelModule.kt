package ru.nikitae57.image_verification.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.nikitae57.common.domain.StateController
import ru.nikitae57.image_verification.domain.ImageVerificationStateController
import ru.nikitae57.image_verification.presentation.viewmodel.ImageVerificationEvent
import ru.nikitae57.image_verification.presentation.viewmodel.ImageVerificationState

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @ViewModelScoped
    @Provides
    fun provideStateController(): StateController<ImageVerificationState, ImageVerificationEvent> {
        return ImageVerificationStateController()
    }
}