package ru.nikitae57.image_verification.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.nikitae57.common.domain.StateController
import ru.nikitae57.common.extension.imageUriToBitmap
import ru.nikitae57.image_verification.domain.ImageToVerify
import javax.inject.Inject

@HiltViewModel
class ImageVerificationViewModel
@Inject constructor(
    @ApplicationContext private val app: Context,
    private val stateController: StateController<ImageVerificationState, ImageVerificationEvent>
) : ViewModel() {

    private var imageToVerify: ImageToVerify? = null
    private val _state = MutableLiveData<ImageVerificationState>()
    val stateLiveData: LiveData<ImageVerificationState> = _state

    init {
        stateController.state
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_state::setValue)
    }

    fun setImageToVerify(image: ImageToVerify): Completable = Completable.fromAction {
        imageToVerify = image
        stateController.addEvent(ImageVerificationEvent.LoadedImage(image))
    }

    fun uriToBitmap(uri: Uri): Single<Bitmap> {
        return app.imageUriToBitmap(uri)
    }

    fun imageAccepted(): Completable = Completable.fromCallable {
        if (imageToVerify == null) {
            throw IllegalStateException("Accepted image, but view model doesn't have reference")
        }

        stateController.addEvent(ImageVerificationEvent.ImageAccepted)
    }

    fun imageIsCancelled(): Completable = Completable.fromAction {
        stateController.addEvent(ImageVerificationEvent.ImageCanceled)
    }
}