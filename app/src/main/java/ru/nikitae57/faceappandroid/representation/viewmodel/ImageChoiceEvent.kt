package ru.nikitae57.faceappandroid.representation.viewmodel

import ru.nikitae57.faceappandroid.domain.model.PickedImage

sealed class ImageChoiceEvent {
    object PickImageFromGallery : ImageChoiceEvent()
    object TakePhoto : ImageChoiceEvent()
    data class ImageIsPicked(val image: PickedImage) : ImageChoiceEvent()
    object ImageIsNotPicked : ImageChoiceEvent()
}
