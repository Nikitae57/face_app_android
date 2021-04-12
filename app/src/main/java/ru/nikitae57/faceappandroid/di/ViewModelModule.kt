package ru.nikitae57.faceappandroid.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.nikitae57.common.domain.StateController
import ru.nikitae57.faceappandroid.domain.ImageChoiceStateController
import ru.nikitae57.faceappandroid.domain.model.CameraConfigWrapper
import ru.nikitae57.faceappandroid.representation.viewmodel.ImageChoiceEvent
import ru.nikitae57.faceappandroid.representation.viewmodel.ImageChoiceState

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @ViewModelScoped
    @Provides
    fun provideTakePhotoConfig(): CameraConfigWrapper {
        return CameraConfigWrapper()
    }

    @ViewModelScoped
    @Provides
    fun provideStateController(): StateController<ImageChoiceState, ImageChoiceEvent> {
        return ImageChoiceStateController()
    }

    @Provides
    @ViewModelScoped
    fun provideSomeValue(): String {
        return "HUY"
    }
}