package ru.nikitae57.image_verification.domain

import ru.nikitae57.common.domain.StateController
import javax.inject.Inject
import ru.nikitae57.image_verification.presentation.viewmodel.ImageVerificationEvent as Event
import ru.nikitae57.image_verification.presentation.viewmodel.ImageVerificationState as State

class ImageVerificationStateController @Inject constructor()
    : StateController<State, Event>(State.ImageVerificationInitialState) {

    override fun addEvent(event: ru.nikitae57.image_verification.presentation.viewmodel.ImageVerificationEvent) {
        super.addEvent(event)

        when(event) {
            is Event.LoadedImage -> emit(State.DisplayingImage(event.image))
            Event.ImageAccepted -> emit(State.ImageIsAccepted)
            Event.ImageCanceled -> emit(State.ImageIsCancelled)
        }
    }
}