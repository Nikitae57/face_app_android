package ru.nikitae57.image_verification.presentation.viewmodel

import ru.nikitae57.image_verification.domain.ImageToVerify

sealed class ImageVerificationState {
    object ImageVerificationInitialState : ImageVerificationState()
    data class DisplayingImage(val image: ImageToVerify) : ImageVerificationState()
    object ImageIsCancelled : ImageVerificationState()
    object ImageIsAccepted : ImageVerificationState()
}