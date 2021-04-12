package ru.nikitae57.image_verification.presentation.viewmodel

import ru.nikitae57.image_verification.domain.ImageToVerify

sealed class ImageVerificationEvent {
    data class LoadedImage(val image: ImageToVerify) : ImageVerificationEvent()
    object ImageCanceled : ImageVerificationEvent()
    object ImageAccepted : ImageVerificationEvent()
}