package ru.nikitae57.faceappandroid.di

import com.esafirm.imagepicker.features.cameraonly.CameraOnlyConfig
import com.esafirm.imagepicker.features.common.BaseConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.nikitae57.faceappandroid.domain.model.CameraConfigWrapper

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @ViewModelScoped
    @Provides
    fun provideTakePhotoConfig(): CameraConfigWrapper {
        return CameraConfigWrapper()
    }
}