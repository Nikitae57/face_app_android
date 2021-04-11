package ru.nikitae57.faceappandroid.representation.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.toPublisher
import com.esafirm.imagepicker.features.ImagePickerLauncher
import com.esafirm.imagepicker.features.registerImagePicker
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.nikitae57.common.representation.UiStateListener
import ru.nikitae57.faceappandroid.databinding.FragmentImageChoiceBinding
import ru.nikitae57.faceappandroid.domain.model.PickedImage
import ru.nikitae57.faceappandroid.representation.viewmodel.ImageChoiceState
import ru.nikitae57.faceappandroid.representation.viewmodel.ImageChoiceViewModel
import timber.log.Timber

@AndroidEntryPoint
class ImageChoiceFragment : Fragment(),
    UiStateListener<ImageChoiceState> {

    private var _binding: FragmentImageChoiceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ImageChoiceViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    private lateinit var takePhotoLauncher: ImagePickerLauncher
    private val pickImageFromGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        try {
            if (result.resultCode == Activity.RESULT_OK
                && result.data?.data != null
            ) {
                val uri = result.data?.data!!
                viewModel.imageIsPicked(uri)
            } else {
                viewModel.imageIsNotPicked()
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            viewModel.imageIsNotPicked()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenViewModel()
        setUiListeners()
        initPhotoLauncher()
    }

    private fun listenViewModel() = viewModel.apply {
        observeState(stateLiveData)
    }

    private fun setUiListeners() {
        with(binding) {
            ibtnGallery.clicks()
                .subscribeBy(
                    onNext = {
                        viewModel.pickImageFromGallery()
                            .subscribeOn(Schedulers.computation())
                            .subscribe()
                    }
                )

            ibtnCamera.clicks()
                .subscribeBy {
                    viewModel.takePhoto()
                        .subscribeOn(Schedulers.computation())
                        .subscribe()
                }
        }
    }

    private fun initPhotoLauncher() {
        takePhotoLauncher = registerImagePicker { images ->
            when {
                images.isEmpty() -> viewModel.imageIsNotPicked()
                images.size == 1 -> {
                    viewModel.imageIsPicked(images.first())
                        .subscribeOn(Schedulers.computation())
                        .subscribe()
                }
                else -> throw IllegalStateException("Exactly one image expected")
            }
        }
    }

    private fun observeState(stateLiveData: LiveData<ImageChoiceState>) {
        stateLiveData.observe(viewLifecycleOwner) { state ->
            state?.let { listenState(state) }
        }
    }

    override fun listenState(state: ImageChoiceState) {
        when (state) {
            is ImageChoiceState.Initial -> initialState().subscribe()
            is ImageChoiceState.ImageIsPicked -> imagePickedState(state.image).subscribe()
            ImageChoiceState.PickingImageFromGallery -> pickingImageState().subscribe()
            ImageChoiceState.TakingPhoto -> takingPhotoState().subscribe()
        }
    }

    private fun initialState(): Completable = Completable.fromCallable {
        Timber.d("Initial state")
    }

    private fun imagePickedState(image: PickedImage): Completable = Completable.fromCallable {
        Timber.d("Image is picked state: $image")
    }

    private fun takingPhotoState(): Completable {
        return openTakePhotoActivity(takePhotoLauncher)
    }

    private fun pickingImageState(): Completable {
        return openImagePickActivity(pickImageFromGalleryLauncher)
    }

    private fun openImagePickActivity(
        launcher: ActivityResultLauncher<Intent>
    ): Completable = Completable.fromCallable {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        launcher.launch(intent)
    }

    private fun openTakePhotoActivity(launcher: ImagePickerLauncher): Completable {
        return viewModel.getCameraLauncherConfig()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable { config ->
                Completable.fromCallable {
                    launcher.launch(config)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ImageChoiceFragment()
    }
}