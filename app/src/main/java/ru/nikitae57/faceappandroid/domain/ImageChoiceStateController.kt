package ru.nikitae57.faceappandroid.domain

import ru.nikitae57.common.domain.StateController
import ru.nikitae57.faceappandroid.representation.viewmodel.ImageChoiceEvent
import ru.nikitae57.faceappandroid.representation.viewmodel.ImageChoiceState
import javax.inject.Inject

class ImageChoiceStateController @Inject constructor()
    : StateController<ImageChoiceState, ImageChoiceEvent>(ImageChoiceState.Initial) {

    override fun addEvent(event: ImageChoiceEvent) {
        super.addEvent(event)

        when (event) {
            is ImageChoiceEvent.PickImageFromGallery -> emit(ImageChoiceState.PickingImageFromGallery)
            is ImageChoiceEvent.TakePhoto -> emit(ImageChoiceState.TakingPhoto)
            is ImageChoiceEvent.ImageIsNotPicked -> emit(ImageChoiceState.Initial)
            is ImageChoiceEvent.ImageIsPicked -> {
                emit(ImageChoiceState.ImageIsPicked(event.image))
                emit(ImageChoiceState.Initial)
            }
        }
    }
}