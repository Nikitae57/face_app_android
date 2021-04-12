package ru.nikitae57.faceappandroid.representation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esafirm.imagepicker.features.common.BaseConfig
import com.esafirm.imagepicker.model.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.nikitae57.common.domain.StateController
import ru.nikitae57.faceappandroid.domain.model.CameraConfigWrapper
import ru.nikitae57.faceappandroid.domain.model.PickedImage
import javax.inject.Inject

@HiltViewModel
class ImageChoiceViewModel
@Inject constructor(
    private val stateController: StateController<ImageChoiceState, ImageChoiceEvent>,
    private val cameraLauncherConfig: CameraConfigWrapper
) : ViewModel() {

    private val _state = MutableLiveData<ImageChoiceState>()
    val stateLiveData: LiveData<ImageChoiceState> = _state

    init {
        stateController.state
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe(_state::postValue)
    }

    fun getCameraLauncherConfig(): Single<BaseConfig> {
        return Single.just(cameraLauncherConfig.config)
    }

    fun pickImageFromGallery(): Completable = Completable.fromCallable {
        stateController.addEvent(ImageChoiceEvent.PickImageFromGallery)
    }

    fun takePhoto(): Completable = Completable.fromCallable {
        stateController.addEvent(ImageChoiceEvent.TakePhoto)
    }

    fun imageIsPicked(imageUri: Uri): Completable = Completable.fromCallable {
        val image = PickedImage(imageUri)
        stateController.addEvent(ImageChoiceEvent.ImageIsPicked(image))
    }

    fun imageIsPicked(image: Image): Completable = Completable.fromCallable {
        val imageDomain = PickedImage(image.uri)
        stateController.addEvent(ImageChoiceEvent.ImageIsPicked(imageDomain))
    }

    fun imageIsNotPicked(): Completable = Completable.fromCallable {
        stateController.addEvent(ImageChoiceEvent.ImageIsNotPicked)
    }
}