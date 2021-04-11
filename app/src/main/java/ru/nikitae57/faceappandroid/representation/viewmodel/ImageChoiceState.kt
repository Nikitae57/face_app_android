package ru.nikitae57.faceappandroid.representation.viewmodel

import ru.nikitae57.faceappandroid.domain.model.PickedImage

sealed class ImageChoiceState {
    object Initial : ImageChoiceState()
    object PickingImageFromGallery : ImageChoiceState()
    object TakingPhoto : ImageChoiceState()
    data class ImageIsPicked(val image: PickedImage) : ImageChoiceState()
}