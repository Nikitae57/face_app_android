package ru.nikitae57.common.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import io.reactivex.rxjava3.core.Single
import timber.log.Timber

fun Context.imageUriToBitmap(uri: Uri): Single<Bitmap> {
    return Single.create {
        try {
            val bitmap = if(Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    uri
                )
            } else {
                val source = ImageDecoder.createSource(this.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            it.onSuccess(bitmap)
        } catch (ex: Exception) {
            Timber.e(ex)
            it.onError(ex)
        }
    }
}