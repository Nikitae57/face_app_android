package ru.nikitae57.image_verification.presentation.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.nikitae57.common.representation.UiStateListener
import ru.nikitae57.image_verification.databinding.FragmentImageVerificationBinding
import ru.nikitae57.image_verification.domain.ImageToVerify
import ru.nikitae57.image_verification.presentation.viewmodel.ImageVerificationState
import ru.nikitae57.image_verification.presentation.viewmodel.ImageVerificationViewModel
import timber.log.Timber

private const val ARG_IMG_TO_VERIFY = "IMG_TO_VERIFY"

@AndroidEntryPoint
class ImageVerificationFragment : Fragment(), UiStateListener<ImageVerificationState> {

    private var _binding: FragmentImageVerificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ImageVerificationViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUiListeners()
        arguments?.let {
            val imageToVerify = ImageToVerify(it.getParcelable(ARG_IMG_TO_VERIFY)!!)
            listenViewModel(imageToVerify)
        }
    }

    private fun listenViewModel(imageToVerify: ImageToVerify) {
        viewModel.apply {
            observeState(stateLiveData)
            setImageToVerify(imageToVerify).subscribe()
        }
    }

    private fun setUiListeners() {
        binding.ibCancel.clicks()
            .subscribeBy {
                viewModel.imageIsCancelled().subscribe()
            }
    }

    private fun observeState(stateLiveData: LiveData<ImageVerificationState>) {
        stateLiveData.observe(viewLifecycleOwner) { state ->
            state?.let(::applyState)
        }
    }

    override fun applyState(state: ImageVerificationState) {
        when(state) {
            ImageVerificationState.ImageVerificationInitialState -> {} // Ignored
            is ImageVerificationState.DisplayingImage -> displayingImageState(state.image).subscribe()
            ImageVerificationState.ImageIsAccepted -> imageIsAcceptedState().subscribe()
            ImageVerificationState.ImageIsCancelled -> imageIsCancelledState().subscribe()
        }
    }

    private fun displayingImageState(image: ImageToVerify) = Completable.fromCallable {
        compositeDisposable.add(
            viewModel.uriToBitmap(image.uri)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    binding.ivImageToVerify.setImageBitmap(it)
                }
        )
    }

    private fun imageIsAcceptedState() = Completable.fromCallable {
        Timber.d("Image is accepted!")
    }

    private fun imageIsCancelledState() = Completable.fromCallable {
        Timber.d("Image is cancelled!")
        parentFragmentManager.popBackStack()
    }

    companion object {
        @JvmStatic
        fun newInstance(imageToVerifyUri: Uri) =
            ImageVerificationFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_IMG_TO_VERIFY, imageToVerifyUri)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}