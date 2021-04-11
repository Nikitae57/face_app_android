package ru.nikitae57.faceappandroid.domain.model

import com.esafirm.imagepicker.features.cameraonly.CameraOnlyConfig
import javax.inject.Inject

class CameraConfigWrapper @Inject constructor() {
    val config = CameraOnlyConfig()
}